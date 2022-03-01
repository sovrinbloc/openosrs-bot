package net.runelite.client.plugins.adonaifarmer;

import com.google.inject.Provides;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.external.adonai.ExtUtils;
import net.runelite.client.external.adonai.mouse.ScreenPosition;
import net.runelite.client.external.adonaicore.npc.NPCs;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.adonaicore.Adonai;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	@Override
	protected void startUp()
	{
		startTime = Instant.now();
		overlayManager.add(overlay);
		Adonai.client = client;
		initializeExecutorService();
	}

	Instant startTime;
	Instant next;

	@Subscribe
	public void onGameTick(GameTick tick)
			throws InterruptedException
	{
		findMasterFarmer();
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
	private void findMasterFarmer()
			throws InterruptedException
	{

		assert client.isClientThread();

		NPC farmer = NPCs.findNearestNPC("Master Farmer");
		if (farmer == null)
		{
			log.info("Not found");
			return;
		}
		log.info("Farmer found");
		dropItemsExcept();
		LocalPoint localLocation = farmer.getLocalLocation();
		Point      point         = Perspective.localToCanvas(client, localLocation, client.getPlane());
		if (point == null)
		{
			log.info("No point");
			return;
		}

		Point pt = ScreenPosition.getScreenPosition(point);
		log.info("Screen position: {}, {}", pt.getX(), pt.getY());

		// this works
		int itemSize = ExtUtils.Inventory.getInventorySize();

		log.info("Inventory size: {}", itemSize);
		// testing to see if this works

		boolean stopThieving = itemSize >= 28;
		if (stopThieving)
		{
			System.out.println("We should be stopping thieving at this point.");
			return;
		}
		moveToFarmer(pt);
	}
	
	void dropItemsExcept()
	{
		List<Integer> keepItems = new ArrayList<>(){
			{
				add(ItemIdentification.HARRALANDER_SEED.itemIDs[0]);
				add(ItemIdentification.RANARR_SEED.itemIDs[0]);
				add(ItemIdentification.TOADFLAX_SEED.itemIDs[0]);
				add(ItemIdentification.IRIT_SEED.itemIDs[0]);
				add(ItemIdentification.AVANTOE_SEED.itemIDs[0]);
				add(ItemIdentification.KWUARM_SEED.itemIDs[0]);
				add(ItemIdentification.SNAPDRAGON_SEED.itemIDs[0]);
				add(ItemIdentification.CADANTINE_SEED.itemIDs[0]);
				add(ItemIdentification.LANTADYME_SEED.itemIDs[0]);
				add(ItemIdentification.DWARF_WEED_SEED.itemIDs[0]);
				add(ItemIdentification.TORSTOL_SEED.itemIDs[0]);
				add(ItemIdentification.JANGERBERRY_SEED.itemIDs[0]);
				add(ItemIdentification.POISON_IVY_SEED.itemIDs[0]);
				add(ItemIdentification.SNAPE_GRASS_SEED.itemIDs[0]);
						add(ItemIdentification.SEED_BOX.itemIDs[0]);
		}};
		ExtUtils.Inventory.init(itemManager);
		List<ExtUtils.Inventory.InventoryItem> itemsExcept = ExtUtils.Inventory.getItemsExcept(keepItems);
		for (ExtUtils.Inventory.InventoryItem i :
				itemsExcept)
		{
			Rectangle itemBounds = ExtUtils.Inventory.invBounds(i.getItem()
					.getId());
			log.info("Inventory Item: {}, Bounds: {}, {}", i.getName(), itemBounds.getX(), itemBounds.getY());
		}
	}

	public Point getScreenPosition(Point point)
	{
		java.awt.Point locationOnScreen = client.getCanvas().getLocationOnScreen();
		return new Point(point.getX() + (int)locationOnScreen.getX(), point.getY() + (int)locationOnScreen.getY());
	}

	private void moveToFarmer(Point pt)
	{
		log.info("We are inside moving farmer;");
		executorService.submit(() ->
		{
			try
			{
				URL url = new URL("http://0.0.0.0:4567/track/" + pt.getX() + "/" + pt.getY());
				InputStream is = url.openConnection()
						.getInputStream();
			}
			catch (Exception e)
			{
				log.info("Could do nothing");
			}
		});
	}


	@Inject
	private ItemManager itemManager;


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

}
