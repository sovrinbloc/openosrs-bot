package net.runelite.client.plugins.adonaibanker;


import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.external.adonai.ExtUtils;
import net.runelite.client.external.adonai.mouse.ScreenPosition;
import net.runelite.client.external.adonaicore.ClickService;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.adonaicore.Adonai;
import net.runelite.client.plugins.adonairandoms.RandomEventDismissPlugin;

import javax.inject.Inject;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@PluginDescriptor(
		name = "Adonai Banker",
		description = "Goes to the bank and deposits or withdraws items.",
		enabledByDefault = false
)
@Slf4j
public class AdonaiBankerPlugin extends Plugin
{
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
		if (state == PlayerState.RANDOM_EVENT)
		{
			return;
		}

		if (!isBankOpen())
		{
			openBank();
		}

		if (isBankOpen())
		{
			depositAll();
		}

		if (isBankOpen())
		{
			closeBank();
		}

	}

	private void openBank()
	{
		GameObject booth = ExtUtils.AdonaiGameObjects.findNearestGameObject(BankIds.bankBoothIds.stream()
				.mapToInt(i -> i)
				.toArray());
		LocalPoint localLocation = booth.getLocalLocation();
		Point      point         = Perspective.localToCanvas(client, localLocation, client.getPlane());

		Point bankPos = ScreenPosition.getScreenPosition(point);
		click(bankPos);

	}

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

	@Provides
	AdonaiBankerConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AdonaiBankerConfig.class);
	}

	private void updateConfig()
	{
	}

	private void sendClick(Point clickPoint)
	{
		log.info("Sending click");
		try
		{
			ClickService.click(clickPoint.getX(), clickPoint.getY());
		}
		catch (Exception e)
		{
			log.info("Could not connect to bot server.");
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

	private void click(Point clickPoint)
	{
		executorService.submit(() ->
		{
			sendClick(clickPoint);
		});
	}

	enum PlayerState
	{
		RANDOM_EVENT,
		INVENTORY_FULL,
		SHOULD_BANK_OR_DROP,
		BANKING,
		SHOULD_STOP_BANKING
	}

	PlayerState state = PlayerState.SHOULD_STOP_BANKING;

	private PlayerState defineState()
	{
		int logsInInventory = ExtUtils.Inventory.getNonStackableInventoryRectanglesExceptByName(new ArrayList<>()
				{{
					add("Some Name");
				}})
				.size();
		int nonStackableInventorySize = ExtUtils.Inventory.getAllNonStackableInventoryCount();
		log.info("Non-Stackable inventory size: {}", nonStackableInventorySize);

		if (RandomEventDismissPlugin.hasRandomEvent())
		{
			// has random event
			state = PlayerState.RANDOM_EVENT;
			return state;
		}

		// is the bank open?
		if (isBankOpen())
		{
			log.info("Is banking.");
			state = PlayerState.BANKING;
			return state;
		}

		// they are not banking so now what?

		// their inventory is empty...
		if (nonStackableInventorySize <= 0)
		{
			state = PlayerState.SHOULD_STOP_BANKING;
			return state;
		}

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
			state = PlayerState.SHOULD_STOP_BANKING;
		}

		// just start chopping
		state = PlayerState.SHOULD_STOP_BANKING;
		return state;
	}

	private int getNonstackableInventorySize()
	{
		return ExtUtils.Inventory.getNonStackableInventoryRectanglesExceptById(new ArrayList<>())
				.size();
	}


	private boolean isBankOpen()
	{
		Widget bankContainer = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
		if (bankContainer == null || bankContainer.isSelfHidden())
		{
			return false;
		}
		return true;
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		boolean bankOpen = event.getGroupId() == WidgetID.BANK_GROUP_ID;
	}

	boolean isDeposit = false;

	void depositAll()
	{
		if (!isBankOpen())
		{
			return;
		}
		isDeposit = true;
		Widget bankItemContainer = client.getWidget(WidgetInfo.BANK_DEPOSIT_INVENTORY);
		Point depositClickPoint = ScreenPosition.getScreenPosition(ExtUtils.AdonaiScreen.getClickPoint(
				bankItemContainer.getBounds()));
		click(depositClickPoint);
	}

	void closeBank()
	{
		if (!isBankOpen())
		{
			return;
		}
		isDeposit = true;
		Widget bankItemContainer = client.getWidget(12, 2)
				.getChild(11);
		Point exitClickPoint = ScreenPosition.getScreenPosition(ExtUtils.AdonaiScreen.getClickPoint(
				bankItemContainer.getBounds()));
		click(exitClickPoint);
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired event)
	{
		if (event.getScriptId() == ScriptID.BANKMAIN_BUILD)
		{
			// Compute bank prices using only the shown items so that we can show bank value during searches
			final Widget        bankItemContainer = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
			final ItemContainer bankContainer     = client.getItemContainer(InventoryID.BANK);
			final Widget[]      children          = bankItemContainer.getChildren();
			long                geTotal           = 0, haTotal = 0;

			if (bankContainer != null && children != null)
			{
				log.debug("Computing bank price of {} items", bankContainer.size());
				log.info(
						"Bank canvas location: {}",
						bankItemContainer.getCanvasLocation()
								.toString()
				);

				// The first components are the bank items, followed by tabs etc. There are always 816 components regardless
				// of bank size, but we only need to check up to the bank size.
				Map<Integer, Map<Integer, Item>> bankMap = new HashMap<Integer, Map<Integer, Item>>();
				for (int i = 0; i < bankContainer.size(); ++i)
				{
					Widget child = children[i];
					if (child != null && !child.isSelfHidden() && child.getItemId() > -1)
					{
						geTotal += (long) itemManager.getItemPrice(child.getItemId()) * child.getItemQuantity();
						log.info(
								"Widget {}: id: {}, child name: {}, (x, y): ({}, {})",
								i,
								child.getItemId(),
								child.getName(),
								child.getBounds()
										.getX(),
								child.getBounds()
										.getY()
						);
						log.info(
								"Canvas: (x, y): ({}, {})",
								child.getCanvasLocation()
										.getX(),
								child.getCanvasLocation()
										.getY()
						);

						//						Rectangle bankBounds = bankItemContainer.getBounds();
						//						log.info("Is item visible? {}", bankBounds.contains(ScreenMath.convertToPoint(bankItemContainer.getCanvasLocation())));
						log.info(
								"Item item {}: id: {}",
								i,
								bankContainer.getItem(i)
										.getId()
						);

					}
				}

				Point bankCanvas = bankItemContainer.getCanvasLocation();
				int   width      = bankItemContainer.getWidth();
				int   height     = bankItemContainer.getHeight();
				int   x          = bankItemContainer.getBounds().x;
				int   y          = bankItemContainer.getBounds().y;
				String bounds = bankItemContainer.getBounds()
						.toString();
				int originalX = bankItemContainer.getOriginalX();
				int originalY = bankItemContainer.getOriginalY();
				log.info("width, height: ({}, {})", width, height);
				log.info("bounds: {}, (x, y): ({}, {})", bounds.toString(), x, y);
				log.info("originalX/Y: (x, y): ({}, {})", originalX, originalY);
				log.info(
						"originalWidth/Height: ({}, {})",
						bankItemContainer.getOriginalWidth(),
						bankItemContainer.getOriginalHeight()
				);


				Widget bankTitle = client.getWidget(WidgetInfo.BANK_TITLE_BAR);
				//				bankTitle.setText(bankTitle.getText() + createValueText(geTotal, haTotal));
			}
		}
		else if (event.getScriptId() == ScriptID.BANKMAIN_SEARCH_REFRESH)
		{
			// vanilla only lays out the bank every 40 client ticks, so if the search input has changed,
			// and the bank wasn't laid out this tick, lay it out early
			final String inputText = client.getVar(VarClientStr.INPUT_TEXT);
		}
	}

	public boolean isBankItemOnScreen(int itemId)
	{
		// this is the item container (we now need to get
		final Widget        bankItemContainer = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
		final ItemContainer bankContainer     = client.getItemContainer(InventoryID.BANK);
		if (bankItemContainer.isHidden())
		{
			return false;
		}

		Point bankContainerCanvas = bankItemContainer.getCanvasLocation();
		int   width               = bankItemContainer.getWidth();
		int   height              = bankItemContainer.getHeight();

		Rectangle bankContainerBounds = bankItemContainer.getBounds();
		int       x                   = bankContainerBounds.x;
		int       y                   = bankContainerBounds.y;


		final Widget[] children = bankItemContainer.getChildren();
		for (int i = 0; i < bankContainer.size(); ++i)
		{
			Widget child = children[i];
			if (child == null || child.isSelfHidden() || child.getItemId() <= -1 || child.getId() != itemId)
			{
				continue;
			}



			// get the attributes of the item to see if it is within the visible container.
			Rectangle childBounds = child.getBounds();
			int       childHeight = child.getHeight();

			Point childCanvasPoint = child.getCanvasLocation();

			// check if the item is on the screen
			return bankContainerBounds.contains(child.getBounds());
		}

		final Widget bankScrollBar = client.getWidget(WidgetInfo.BANK_SCROLLBAR);
		if (bankScrollBar.isHidden())
		{

		}
		Widget upScroll   = bankScrollBar.getChildren()[4];
		Widget downScroll = bankScrollBar.getChildren()[5];

		// todo: finish this
		return true;
	}

	public boolean scrollToBankItem(int itemId)
	{
		// this is the item container (we now need to get
		final Widget        bankItemContainer = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
		final ItemContainer bankContainer     = client.getItemContainer(InventoryID.BANK);
		if (bankItemContainer.isHidden())
		{
			return false;
		}
		Point bankContainerCanvas = bankItemContainer.getCanvasLocation();
		int   width               = bankItemContainer.getWidth();
		int   height              = bankItemContainer.getHeight();

		Rectangle bankContainerBounds = bankItemContainer.getBounds();
		int       x                   = bankContainerBounds.x;
		int       y                   = bankContainerBounds.y;


		final Widget[] children = bankItemContainer.getChildren();
		Widget item = null;
		for (int i = 0; i < bankContainer.size(); ++i)
		{
			Widget child = children[i];
			if (child == null || child.isSelfHidden() || child.getItemId() <= -1 || child.getId() != itemId)
			{
				continue;
			}

			item = child;
		}

		// get the attributes of the item to see if it is within the visible container.
		Rectangle childBounds = item.getBounds();
		int       childHeight = item.getHeight();

		Point childCanvasPoint = item.getCanvasLocation();

		final Widget bankScrollBar = client.getWidget(WidgetInfo.BANK_SCROLLBAR);
		if (bankScrollBar.isHidden())
		{
			return false;
		}
		Widget[] bankChildren  = bankScrollBar.getChildren();

		assert bankChildren != null;
		Widget upScroll   = bankChildren[4];
		Widget downScroll = bankChildren[5];

		if (bankContainerBounds.contains(item.getBounds()))
		{
			return true;
		}

		boolean scrollDown = false;
		int yPrime = 0;
		if (item.getBounds().getY() > bankContainerBounds.getY() + bankContainerBounds.getHeight())
		{
			scrollDown = true;
			yPrime = (int)(item.getBounds().getY() - bankContainerBounds.getY() + bankContainerBounds.getHeight());
		}

		// todo: finish this
		return true;
	}
}