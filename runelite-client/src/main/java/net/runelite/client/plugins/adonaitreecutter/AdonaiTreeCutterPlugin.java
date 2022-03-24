package net.runelite.client.plugins.adonaitreecutter;


import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.external.adonai.ExtUtils;
import net.runelite.client.external.adonai.mouse.ScreenPosition;
import net.runelite.client.external.adonaicore.ClickService;
import net.runelite.client.external.adonaicore.objects.Objects;
import net.runelite.client.external.adonaicore.toolbox.Text;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.adonaicore.Adonai;
import net.runelite.client.plugins.adonairandoms.RandomEventDismissPlugin;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@PluginDescriptor(
		name = "Adonai Tree Cutter",
		description = "Cuts Trees Until Full",
		enabledByDefault = false
)
@Slf4j
public class AdonaiTreeCutterPlugin extends Plugin
{
	private static final Set<Integer> CHOPPING_ANIMATIONS = ImmutableSet.of(
			AnimationID.WOODCUTTING_BRONZE,
			AnimationID.WOODCUTTING_IRON,
			AnimationID.WOODCUTTING_STEEL,
			AnimationID.WOODCUTTING_BLACK,
			AnimationID.WOODCUTTING_MITHRIL,
			AnimationID.WOODCUTTING_ADAMANT,
			AnimationID.WOODCUTTING_RUNE,
			AnimationID.WOODCUTTING_DRAGON,
			AnimationID.WOODCUTTING_DRAGON_OR
	);
	@Inject
	private Client client;

	@Inject
	private Notifier notifier;

	@Inject
	private ItemManager itemManager;

	@Override
	protected void startUp()
	{

		Adonai.client = client;
		ExtUtils.initialize(client);
		ScreenPosition.initialize(client);
		initializeExecutorService();
		ExtUtils.Inventory.init(itemManager);
		updateConfig();
	}

	@Override
	protected void shutDown()
			throws Exception
	{
		executorService.shutdown();
	}

	@Subscribe
	public void onGameTick(GameTick tick)
			throws InterruptedException, IOException
	{

		// this works
		int inventorySize = getNonstackableInventorySize();
		log.info("Ticking... Inventory size: {}", inventorySize);

		PlayerState tmpPlayerState = defineState();
		log.info("Player state: {}", tmpPlayerState);


		switch (state)
		{
			case CHOPPING:
				log.info("Does nothing because the player is still chopping.");
				return;
			case SHOULD_START_CHOPPING:
				log.info("Player is about to chop");
				chop();
				return;
			case BANKING:
			case SHOULD_BANK_OR_DROP:
			case INVENTORY_FULL:
			case NOT_CHOPPING:
		}
	}

	private void chop()
	{

		assert client.isClientThread();

		// set player to thieving
		state = PlayerState.CHOPPING;

		GameObject cuttingSpot = Objects.findNearestGameObject(tree ->
		{
			String treeName = Text.removeWhitespace(tree.getName()
					.toLowerCase());
			boolean contains = treeNames.contains(treeName);
			if (contains)
			{
				log.info("Tree names contains it!: {}", treeName);
			}
			return contains;
		});

		if (cuttingSpot == null)
		{
			log.info("Spot NOT found");
			return;
		}
		log.info("Found Tree");

		LocalPoint localLocation = cuttingSpot.getLocalLocation();
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

		doClick(pt);
	}


	private List<String> treeNames = new ArrayList<>();


	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup()
				.toLowerCase()
				.contains("bot"))
		{
			updateConfig();
		}
	}


	private boolean isChopping()
	{
		return (CHOPPING_ANIMATIONS.contains(client.getLocalPlayer()
				.getAnimation()));
	}


	@Provides
	AdonaiTreeCutterConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AdonaiTreeCutterConfig.class);
	}

	private void updateConfig()
	{
		setTreeNames();
	}

	private void setTreeNames()
	{
		if (!AdonaiTreeCutterConfig.treeNames()
				.contains(","))
		{
			treeNames.add(Text.removeWhitespace(AdonaiTreeCutterConfig.treeNames()
					.toLowerCase()));
		}
		List<String> tmpTreeNames = Arrays.stream(AdonaiTreeCutterConfig.treeNames()
						.split(","))
				.collect(Collectors.toList());
		tmpTreeNames.forEach(t -> treeNames.add(Text.removeWhitespace(t.toLowerCase())));
	}

	private void sendClick(Point clickPoint)
	{
		log.info("Sending click");
		try
		{
			ClickService.track(clickPoint.getX(), clickPoint.getY());
		}
		catch (Exception e)
		{
			log.info("Could do nothing on seed box");
		}
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

	private void doClick(Point clickPoint)
	{
		executorService.submit(() ->
		{
			sendClick(clickPoint);
		});
	}

	enum PlayerState
	{
		RANDOM_EVENT,
		CHOPPING,
		NOT_CHOPPING,
		INVENTORY_FULL,
		SHOULD_BANK_OR_DROP,
		BANKING,
		SHOULD_START_CHOPPING
	}

	PlayerState state = PlayerState.SHOULD_START_CHOPPING;

	private PlayerState defineState()
	{
		log.info("Player animation: {}",
				client.getLocalPlayer()
						.getAnimation()
		);
		int logsInInventory = ExtUtils.Inventory.getNonStackableInventoryRectanglesExceptByName(treeNames)
				.size();
		int nonStackableInventorySize = ExtUtils.Inventory.getAllNonStackableInventoryCount();
		log.info("Nonstackable inventory size: {}", nonStackableInventorySize);

		if (RandomEventDismissPlugin.hasRandomEvent())
		{
			// has random event
			state = PlayerState.RANDOM_EVENT;
			return state;
		}

		// are they chopping?
		if (isChopping())
		{
			log.info("Is chopping");
			state = PlayerState.CHOPPING;
			return state;
		}

		// they are not chopping so now what?

		// is their inventory full? if so, say that they should bank or drop (if not already)
		if (nonStackableInventorySize >= 28 && state != PlayerState.SHOULD_BANK_OR_DROP)
		{
			// they are no longer fishing because their inventory is full.
			// drop fish.
			state = PlayerState.SHOULD_BANK_OR_DROP;
			return state;
		}

		// they are still dropping or banking
		if (state == PlayerState.SHOULD_BANK_OR_DROP && logsInInventory > 0)
		{
			// return that they should still drop or bank
			return state;
		}

		// they are not dropping, not chopping, and their inventory is not full
		if (nonStackableInventorySize < 28)
		{
			// start chopping
			state = PlayerState.SHOULD_START_CHOPPING;
		}

		// just start chopping
		state = PlayerState.SHOULD_START_CHOPPING;
		return state;
	}

	private int getNonstackableInventorySize()
	{
		return ExtUtils.Inventory.getNonStackableInventoryRectanglesExceptById(new ArrayList<>())
				.size();
	}
}