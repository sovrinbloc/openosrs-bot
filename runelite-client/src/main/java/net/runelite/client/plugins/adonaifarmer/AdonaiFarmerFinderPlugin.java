package net.runelite.client.plugins.adonaifarmer;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.external.adonai.ExtUtils;
import net.runelite.client.external.adonai.mouse.ScreenPosition;
import net.runelite.client.plugins.adonairandoms.*;
import net.runelite.client.external.adonaicore.ClickService;
import net.runelite.client.external.adonaicore.npc.NPCs;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.adonaicore.Adonai;
import net.runelite.client.ui.overlay.OverlayManager;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.awt.*;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@PluginDescriptor(
		name = "Adonai Farmer Finder",
		description = "Finds Farmer on Canvas"
)
@Slf4j
@SuppressWarnings("unused")
public class AdonaiFarmerFinderPlugin extends Plugin
{
	static final String CONFIG_GROUP = "adonaifarmerfinder";

	@Provides
	ItemIdentificationConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ItemIdentificationConfig.class);
	}


	@Inject
	private Client client;

	@Inject
	private ItemIdentificationConfig config;

	@Inject
	private OverlayManager overlayManager;


	@Inject
	private AdonaiFarmerFinderOverlay overlay;


	@Inject
	private ItemManager itemManager;

	// mutex for updating the count of dropped items in new thread.
	private ReentrantLock mutex = new ReentrantLock();


	enum PlayerState
	{
		THIEVING,
		DROPPING,
		NOTHING,
		STARTING,
		SHUTTING_DOWN

	}

	public static PlayerState state = PlayerState.NOTHING;


	@Override
	protected void startUp()
	{
		startTime = Instant.now();
		overlayManager.add(overlay);

		Adonai.client = client;
		initializeExecutorService();
		ExtUtils.Inventory.init(itemManager);

		state = PlayerState.STARTING;
		ScreenPosition.initialize(client);
	}

	Instant startTime;

	Instant next;

	int dropCount = 0;


	@Subscribe
	public void onGameTick(GameTick tick)
			throws InterruptedException
	{

		// check Random
		if (RandomEventEasyPlugin.hasRandomEvent())
		{
			sendClick(ScreenPosition.getNpcScreenPoint(RandomEventEasyPlugin.getCurrentRandomEvent()));
		}
		// this works
		int inventorySize = ExtUtils.Inventory.getInventorySize();
		if (inventorySize < 28 && (state != PlayerState.DROPPING || dropCount <= 0))
		{
			findAndThieveMasterFarmer();
			return;
		}
		log.info("Inventory size to drop: {}", inventorySize);
		if (state != PlayerState.DROPPING)
		{
			drop();
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

	@Override
	protected void shutDown()
	{
		executorService.shutdown();
		overlayManager.remove(overlay);
	}

	/**
	 *
	 */
	private void findAndThieveMasterFarmer()
			throws InterruptedException
	{

		assert client.isClientThread();

		// set player to thieving
		state = PlayerState.THIEVING;

		NPC farmer = NPCs.findNearestNPC("Master Farmer");
		if (farmer == null)
		{
			log.info("Not found");
			return;
		}
		log.info("Farmer found");

		Point pt = getNpcScreenPoint(farmer);
		if (pt == null)
		{
			return;
		}

		// this works
		int itemSize = ExtUtils.Inventory.getInventorySize();

		log.info("Inventory size: {}", itemSize);
		// testing to see if this works
		if (itemSize < 28)
		{
			trackFarmer(pt);
		}
	}

	@Nullable
	public Point getNpcScreenPoint(NPC farmer)
	{
		LocalPoint localLocation = farmer.getLocalLocation();
		Point      point         = Perspective.localToCanvas(client, localLocation, client.getPlane());
		if (point == null)
		{
			log.info("No point");
			return null;
		}

		Point pt = ScreenPosition.getScreenPosition(point);
		log.info("Screen position: {}, {}", pt.getX(), pt.getY());
		return pt;
	}

	private void drop()
	{
		dropItemsExcept();
		randomClickPoint = null;
	}

	Point randomClickPoint;


	void dropItemsExcept()
	{
		// set player state to DROPPING
		state = PlayerState.DROPPING;

		List<ExtUtils.Inventory.InventoryItem> itemsExcept = ExtUtils.Inventory.getItemsExcept(DropConfig.keepItems);
		dropCount = itemsExcept.size();

		ItemIdentification clickItem = ItemIdentification.SEED_BOX;


		ExtUtils.Inventory.InventoryItem seedBox = ExtUtils.Inventory.getItem(clickItem.itemIDs[0]);
		if (randomClickPoint == null)
		{
			randomClickPoint = ExtUtils.AdonaiScreen.getClickPoint(seedBox.getBounds());
		}

		Point clickPoint = ScreenPosition.getScreenPosition(randomClickPoint);
		doClick(clickPoint);


		Iterator<ExtUtils.Inventory.InventoryItem> iterator = itemsExcept.iterator();
		ExtUtils.Inventory.InventoryItem           iItem    = null;
		for (int i = 0; i < itemsExcept.size(); i++)
		{
			iItem = itemsExcept.get(i);
			Rectangle itemBounds = ExtUtils.Inventory.invBounds(iItem.getItem()
					.getId());

			doClick(ScreenPosition.getScreenPosition(
					ExtUtils.AdonaiScreen.getClickPoint(itemBounds.getBounds())
			));
		}
		log.info("Done dropping");
	}

	private void doClick(Point clickPoint)
	{
		executorService.submit(() ->
		{
			sendClick(clickPoint);
			//			sleep(300);
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

	public Point getScreenPosition(Point point)
	{
		java.awt.Point locationOnScreen = client.getCanvas()
				.getLocationOnScreen();
		return new Point(point.getX() + (int) locationOnScreen.getX(), point.getY() + (int) locationOnScreen.getY());
	}

	private void trackFarmer(Point pt)
	{
		log.info("We are inside moving farmer;");
		executorService.submit(() ->
		{
			sendTrackFarmer(pt);
		});
	}

	private void sendTrackFarmer(Point pt)
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


	int successfulThieve = 0;

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getItemContainer() != client.getItemContainer(InventoryID.INVENTORY))
		{
			return;
		}


		// get the new items added
		for (Item item : event.getItemContainer().getItems())
		{
			// does the keepItems contain this id?
			if (DropConfig.keepItems.contains(item.getId()))
			{

				successfulThieve++;

				// loop through the keep items and see if we can find a match here.
				for (int itemId :
						DropConfig.keepItems)
				{
					if (itemId != item.getId())
					{
						continue;
					}

					// how many seeds are now added
					int difference = 0;

					// if they already had thieved this item in the past
					if (AdonaiFarmerFinderOverlay.thievedItems.containsKey(item.getId()))
					{
						// get the amount already thieved in the past
						int tmpQuantity = AdonaiFarmerFinderOverlay.thievedItems.get(item.getId());

						// don't continue because the seeds were just added to seed box
						if (tmpQuantity > item.getQuantity())
						{
							if (item.getQuantity() == 0)
							{
								continue;
							}
//							AdonaiFarmerFinderOverlay.thievedItems.put(item.getId(), AdonaiFarmerFinderOverlay.thievedItems.get(item.getId()) + item.getQuantity());
						}

						// if there are new seeds added (more than there were before), add to the thieved number
						if (tmpQuantity < item.getQuantity())
						{
							difference = item.getQuantity() - tmpQuantity;
							AdonaiFarmerFinderOverlay.thievedItems.put(item.getId(), AdonaiFarmerFinderOverlay.thievedItems.get(item.getId()) + difference);
						}
						continue;
					}

					AdonaiFarmerFinderOverlay.seedMap.putIfAbsent(item.getId(), ExtUtils.Inventory.getItem(item.getId()).getName());

					// initially add the number of items to the thieved items
					AdonaiFarmerFinderOverlay.thievedItems.put(item.getId(), item.getQuantity());
				}
			}
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
		}
	}

}
