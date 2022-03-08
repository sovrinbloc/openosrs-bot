package net.runelite.client.plugins.adonaifisher;


import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.external.adonai.ExtUtils;
import net.runelite.client.external.adonai.mouse.ScreenPosition;
import net.runelite.client.external.adonaicore.npc.NPCs;
import net.runelite.client.game.FishingSpot;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.adonaicore.Adonai;
import net.runelite.client.plugins.adonairandoms.RandomEventDismissPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@PluginDescriptor(
		name = "Adonai Fisher",
		description = "Fishes"
)
@Slf4j
@SuppressWarnings("unused")
public class AdonaiFisherPlugin extends Plugin
{
	static final String CONFIG_GROUP = "adonaifisher";

	@Provides
	AdonaiFisherConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AdonaiFisherConfig.class);
	}


	@Inject
	private Client client;

	@Inject
	private AdonaiFisherConfig config;

	@Inject
	private OverlayManager overlayManager;


	@Inject
	private AdonaiFisherOverlay overlay;

	@Inject
	private ItemManager itemManager;

	// mutex for updating the count of dropped items in new thread.
	private ReentrantLock mutex = new ReentrantLock();

	public static FishingSession.PlayerState state = FishingSession.PlayerState.NOTHING;

	@Getter(AccessLevel.PACKAGE)
	private final List<NPC> fishingSpots = new ArrayList<>();

	@Getter(AccessLevel.PACKAGE)
	private FishingSpot currentSpot;

	@Getter(AccessLevel.PACKAGE)
	private final FishingSession session = new FishingSession();

	private Instant startTime;

	private Instant next;

	private int dropCount = 0;

	@Override
	protected void startUp()
	{
		startTime = Instant.now();
		overlayManager.add(overlay);

		Adonai.client = client;
		ExtUtils.initialize(client);
		ScreenPosition.initialize(client);
		initializeExecutorService();
		ExtUtils.Inventory.init(itemManager);
		state = FishingSession.PlayerState.NOTHING;
	}

	@Override
	protected void shutDown()
	{
		executorService.shutdown();
		overlayManager.remove(overlay);
		currentSpot = null;
		fishingSpots.clear();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		GameState gameState = gameStateChanged.getGameState();
		if (gameState == GameState.CONNECTION_LOST || gameState == GameState.LOGIN_SCREEN || gameState == GameState.HOPPING)
		{
			fishingSpots.clear();
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.SPAM)
		{
			return;
		}

		if (event.getMessage()
				.contains("You catch a") || event.getMessage()
				.contains("You catch some") ||
				event.getMessage()
						.equals("Your cormorant returns with its catch."))
		{
			session.setLastFishCaught(Instant.now());
		}
	}

	@Subscribe
	public void onInteractingChanged(InteractingChanged event)
	{
		if (event.getSource() != client.getLocalPlayer())
		{
			return;
		}

		final Actor target = event.getTarget();

		if (!(target instanceof NPC))
		{
			return;
		}

		final NPC   npc  = (NPC) target;
		FishingSpot spot = FishingSpot.findSpot(npc.getId());

		if (spot == null)
		{
			return;
		}

		currentSpot = spot;
	}

	@Subscribe
	public void onGameTick(GameTick tick)
			throws InterruptedException, IOException
	{
		// this works
		int inventorySize = getNonstackableInventorySize();
		log.info("Ticking... Inventory size: {}", inventorySize);

		FishingSession.PlayerState tmpPlayerState = defineState();
		log.info("Player is now state: {}", tmpPlayerState);

		if (state == FishingSession.PlayerState.RANDOM_EVENT)
		{
			log.info("Has a random event, so pausing");
			return;
		}

		if (state == FishingSession.PlayerState.FISHING)
		{
			// do nothing.
			log.info("Does nothing because the player is still fishing.");
			return;
		}

		if (state == FishingSession.PlayerState.DROPPING_FISH)
		{
			log.info("Does nothing because the player is still dropping.");
			// keep dropping.
			return;
		}

		// new
		if (state == FishingSession.PlayerState.SHOULD_START_FISHING)
		{
			log.info("Player is about to fish");
			fish();
			return;
		}

		if (state == FishingSession.PlayerState.SHOULD_START_DROPPING)
		{
			log.info("Player is about to start dropping");
			drop();
		}
	}

	private int getNonstackableInventorySize()
	{
		return ExtUtils.Inventory.getNonStackableInventoryRectanglesExceptById(new ArrayList<>())
				.size();
	}

	private FishingSession.PlayerState defineState()
	{
		int fishInInventory = ExtUtils.Inventory.getNonStackableInventoryRectanglesExceptById(DropConfig.keepItems)
				.size();
		int nonStackableInventorySize = getNonstackableInventorySize();
		log.info("Nonstackable inventory size: {}", nonStackableInventorySize);

		if (RandomEventDismissPlugin.hasRandomEvent())
		{
			state = FishingSession.PlayerState.RANDOM_EVENT;
			return state;
		}
		// are they fishing?
		if (isFishing())
		{
			state = FishingSession.PlayerState.FISHING;
			return state;
		}

		// is their inventory full?
		if (nonStackableInventorySize >= 28 && state != FishingSession.PlayerState.DROPPING_FISH)
		{
			// they are no longer fishing because their inventory is full.
			// drop fish.
			state = FishingSession.PlayerState.SHOULD_START_DROPPING;
			return state;
		}

		// are they still dropping?
		if ((state == FishingSession.PlayerState.DROPPING_FISH && fishInInventory > 0) || dropCount > 0)
		{
			// remain dropping.
			state = FishingSession.PlayerState.DROPPING_FISH;
			return state;
		}

		state = FishingSession.PlayerState.SHOULD_START_FISHING;
		return state;
	}

	private boolean isFishing()
	{

		return (client.getLocalPlayer()
				.getInteracting() != null
				&& client.getLocalPlayer()
				.getInteracting()
				.getName()
				.contains(FishingSession.FISHING_SPOT)
				&& client.getLocalPlayer()
				.getInteracting()
				.getGraphic() != GraphicID.FLYING_FISH
				&& FishingSession.FISHING_ANIMATIONS.contains(client.getLocalPlayer()
				.getAnimation()));
	}

	/**
	 * MultiThread for Farmer Test
	 * Herein is the variables for this.
	 */
	private ExecutorService executorService;

	private void initializeExecutorService()
	{
		executorService = Executors.newSingleThreadExecutor();
	}

	/**
	 * Begins fishing the nearest spot.
	 * <p>
	 * This method gets the position on the screen of the nearest fishing spot
	 * and clicks it. It will also set the flag of the user to FISHING so we cannot
	 * drop at the same time. It will stop when full.
	 *
	 * @throws IOException
	 * @implSpec The default implementation will only work with Fly Fishing spots
	 * and also drop them.
	 * @implNote This should add other fish.
	 */
	private void fish()
			throws IOException
	{

		assert client.isClientThread();

		// set player to thieving
		state = FishingSession.PlayerState.FISHING;

		NPC fishingSpot = NPCs.findNearestNPC(n -> n.getName().toLowerCase().contains("fishing spot"));
		if (fishingSpot == null)
		{
			log.info("Rod Spot Not found");
			return;
		}
		log.info("Fishing spot found");

		LocalPoint localLocation = fishingSpot.getLocalLocation();
		Point      point         = Perspective.localToCanvas(client, localLocation, client.getPlane());
		if (point == null)
		{
			log.info("No fishing Point");
			return;
		}

		Point pt = ScreenPosition.getScreenPosition(point);
		log.info("Screen position of Fishing Spot: {}, {}", pt.getX(), pt.getY());

		// this works
		int itemSize = ExtUtils.Inventory.getInventorySize();

		log.info("Inventory size: {}", itemSize);
		// testing to see if this works

		if (!client.getLocalPlayer().isMoving())
		{
			doClick(pt);
		}
	}

	private void drop()
	{
		dropItemsExcept(DropConfig.keepItems);
	}

	void debugMsg(String message, Object ... arguments)
	{
		log.info("DEBUG MESSAGE: " + message, arguments);
	}

	void dropItemsExcept(List<Integer> ids)
	{
		// set player state to DROPPING
		state = FishingSession.PlayerState.DROPPING_FISH;

		List<Rectangle> invBounds = ExtUtils.Inventory.getNonStackableInventoryRectanglesExceptById(ids);

		invBounds.sort(ExtUtils.Inventory.getDropMode(ExtUtils.Inventory.InventoryDropMode.TOP_DOWN_LEFT_RIGHT));

		dropCount = invBounds.size();

		// loop through and drop each of the items in this inventory
		// 	(with support of left-click dropper)
		for (Rectangle bound : invBounds)
		{
			debugMsg("inside DROP function loop");
			doClick(ScreenPosition.getScreenPosition(
					ExtUtils.AdonaiScreen.getClickPoint(bound.getBounds())
			));
		}
		log.info("Done dropping");
	}


	private void doClick(Point clickPoint)
	{
		debugMsg("doClick: {}, {}", clickPoint.getX(), clickPoint.getY());;
		executorService.submit(() ->
		{
			sendClick(clickPoint);
			try
			{
				mutex.lock();
				--dropCount;
				log.info("dropCount: {}", dropCount);
			}
			finally
			{
				mutex.unlock();
			}
		});
	}

	private void sendClick(Point clickPoint)
	{
		try
		{
			ClickService.click(clickPoint.getX(), clickPoint.getY());
		}
		catch (Exception e)
		{
			log.info("Could do nothing on seed box");
		}
	}

	private void trackSpot(Point pt)
	{
		executorService.submit(() ->
		{
			sendFish(pt);
		});
	}

	private void sendFish(Point pt)
	{
		try
		{
			ClickService.sendToServer("http://0.0.0.0:4567/track/" + pt.getX() + "/" + pt.getY());
		}
		catch (Exception e)
		{
			log.info("Could do nothing");
		}
	}


	void getItemNames()
	{
		ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);

		final Item[]                       items          = itemContainer.getItems();
		ExtUtils.Inventory.InventoryItem[] inventoryItems = ExtUtils.Inventory.convertToInventoryItems(items);
		for (ExtUtils.Inventory.InventoryItem item :
				inventoryItems)
		{
			log.info(
					"Inventory item: {}, isStackable: {}, Id: {}",
					item.getName(),
					item.isStackable(),
					item.getItem()
							.getId()
			);
		}
	}

	Map<Integer, Integer> caughtFish = new HashMap<>();

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getItemContainer() != client.getItemContainer(InventoryID.INVENTORY))
		{
			return;
		}

		int quant = 0;

		for (Item item : event.getItemContainer()
				.getItems())
		{
			caughtFish.put(item.getId(), caughtFish.getOrDefault(item.getId(), 0) + item.getQuantity());
			if (caughtFish.containsKey(item.getId()))
			{
				quant++;
			}
		}
	}


	private void inverseSortSpotDistanceFromPlayer()
	{
		if (fishingSpots.isEmpty())
		{
			return;
		}

		final LocalPoint cameraPoint = new LocalPoint(client.getCameraX(), client.getCameraY());
		fishingSpots.sort(
				Comparator.comparingInt(
								// Negate to have the furthest first
								(NPC npc) -> -npc.getLocalLocation()
										.distanceTo(cameraPoint))
						// Order by position
						.thenComparing(NPC::getLocalLocation, Comparator.comparingInt(LocalPoint::getX)
								.thenComparingInt(LocalPoint::getY))
						// And then by id
						.thenComparingInt(NPC::getId)
		);
	}


	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		final NPC npc = event.getNpc();

		if (FishingSpot.findSpot(npc.getId()) == null)
		{
			return;
		}

		fishingSpots.add(npc);
		inverseSortSpotDistanceFromPlayer();
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		final NPC npc = npcDespawned.getNpc();

		fishingSpots.remove(npc);

	}

}
