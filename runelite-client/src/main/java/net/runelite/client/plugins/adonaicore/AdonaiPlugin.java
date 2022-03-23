package net.runelite.client.plugins.adonaicore;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.menu.MenuRow;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.external.adonai.MenuSession;
import net.runelite.client.external.adonai.TabMap;
import net.runelite.client.external.adonai.mouse.ScreenPosition;
import net.runelite.client.external.adonaicore.npc.NPCs;
import net.runelite.client.external.adonaicore.objects.Objects;
import net.runelite.client.external.adonaicore.screen.Screen;
import net.runelite.client.external.adonaicore.toolbox.Calculations;
import net.runelite.client.external.adonaicore.utils.Messages;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.adonaifisher.ClickService;

import javax.inject.Inject;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@PluginDescriptor(
		name = "Adonai Core Plugin",
		description = "Adonai Bot Core"
)
@Slf4j
@SuppressWarnings("unused")
public class AdonaiPlugin extends Plugin
{
	static final String CONFIG_GROUP = "adonaicore";

	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ChatMessageManager chatMessageManager;

	private LocalPoint localLocation;

	private ExtUtils utils;

	@Getter
	private Point minimapLocation;

	private Player player;

	@Inject
	private AdonaiConfig config;

	// to execute things like key press and click -- new thread
	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);

	private ExecutorService executor;

	private int gameTicks = 0;

	private MenuSession adonaiMenu;

	// the CURRENT interacted object, when clicked or walked to, etc... between player and object
	@Getter(AccessLevel.PACKAGE)
	private TileObject interactedObject;

	@Provides
	AdonaiConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AdonaiConfig.class);
	}

	/**
	 * Initialize the adonai client -- including plugging in the tooltip interface
	 */
	@Override
	protected void startUp()
	{
		executor = Executors.newFixedThreadPool(3);

		// this initializes the MenuSession
		adonaiMenu = new MenuSession(client);

		net.runelite.client.external.adonai.ExtUtils.initialize(client);
		ScreenPosition.initialize(client);

		if (Adonai.isNullInitializeClient(client))
		{
			log.info("Initialized Adonai Client.");
		}
		if (Adonai.isNullInitializeChatMessageManager(this.chatMessageManager))
		{
			if (client.getGameState() == GameState.LOGGED_IN)
			{
				Messages.sendChatMessage("Adonai Messages Initialized.");
			}
		}

		try
		{
			if (Adonai.isNullInitializeKeyboard(new Keyboard()))
			{
				log.info("Initialized Adonai Client.");
			}
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}


		this.utils = new ExtUtils(client, Adonai.keyboard);
		this.utils.setChatMessageManager(chatMessageManager);
	}

	/**
	 * Count 1 begin
	 * desc is the description
	 * Count 1 end desc
	 * <p>
	 * // desc: description explanation
	 * Gets a copy of the menu every single time the client ticks.
	 *
	 * @param tick carries the information of the tick
	 */
	@Subscribe
	public void onClientTick(ClientTick tick)
	{
		adonaiMenu.getUpdatedMenu();
	}

	/**
	 * @apiNote right clicks
	 * @param npcName
	 * @param menuItem
	 */
	void rightClickNpc(String npcName, String menuItem)
	{
		assert client.isClientThread();

		NPC npc = NPCs.findNearestNPC(n -> n.getName()
				.toLowerCase()
				.contains(npcName));
		if (npc == null)
		{
			log.info("NPC Not found");
			return;
		}
		log.info("NPC found");

		LocalPoint localLocation = npc.getLocalLocation();
		Point      point         = Perspective.localToCanvas(client, localLocation, client.getPlane());
		if (point == null)
		{
			log.info("No NPC Point");
			return;
		}

		Point npcScreenPosition = ScreenPosition.getScreenPosition(point);
		log.info("Screen position of NPC: {}, {}", npcScreenPosition.getX(), npcScreenPosition.getY());


		log.info("Trying to click");
		// move mouse over tile and right click
		doMouse(npcScreenPosition, ClickType.Right);
		// gets the npc tile
		//		TileObject obj = Objects.getTileObject(updatedMenu.get(0)
		//				.getMenuTargetIdentifiers());

		AtomicInteger index = new AtomicInteger(-1);


		MenuRow menuRow = null;
		for (MenuRow row : adonaiMenu.getAdonaiMenu()
				.getAllMenuRows())
		{
			log.info("Traversing Menu Item. Target: {}, Item: {}, Row: {}", row.getEntry(), row.getFullText(), row);
			if (!row.getTarget()
					.toLowerCase()
					.contains(npcName.toLowerCase()))
			{
				log.info("Could not find the target: {} inside: {}", npcName.toLowerCase(), row.getTarget().toLowerCase());
				continue;
			}

			if (!row.getFullText()
					.toLowerCase()
					.contains(menuItem.toLowerCase()))
			{
				log.info("Could not find the menu item: {} inside: {}", menuItem.toLowerCase(), row.getFullText().toLowerCase());
				continue;
			}
			log.info(
					"Found the menu item: {}, {}",
					row.getIndex(),
					adonaiMenu.getAdonaiMenu()
							.getAllMenuRows().get(row.getIndex())
							.getFullText()
			);
			menuRow = row;
			break;
		}


		log.info("Now having received the menu item, we perform what is necessary.");
		// send move mouse to row rectangle point on screen
		Rectangle menuHitBox         = menuRow.getHitBox();
		Point     menuClickPoint = net.runelite.client.external.adonai.ExtUtils.AdonaiScreen.getClickPoint(menuHitBox);
		Point     screenPosition = ScreenPosition.getScreenPosition(menuClickPoint);

		// send left click
		log.info("Trying to click option ({}) at canvas hitbox position {}, canvas clickPosition {}, (screen clickPosition: {}).", menuRow.getOption(), menuHitBox, menuClickPoint, screenPosition);
		doMouse(screenPosition, ClickType.Left);
	}

	enum ClickType
	{
		Left,
		Right,
		Move
	}

	private void doMouse(Point clickPoint, ClickType clickType)
	{
		executor.submit(() ->
		{
			sendClick(clickPoint, clickType);
		});
	}

	private void sendClick(Point clickPoint, ClickType clickType)
	{
		try
		{
			switch (clickType)
			{
				case Left:
					ClickService.click(clickPoint.getX(), clickPoint.getY());
					return;
				case Right:
					ClickService.rightClick(clickPoint.getX(), clickPoint.getY());
					return;
				default:
					ClickService.move(clickPoint.getX(), clickPoint.getY());
			}
		}
		catch (Exception e)
		{
			log.info("Could send the click... But let's pretend we just sent a {} to {}", clickType, clickPoint);
		}
	}


	final int TICK_MOD = 20;
	int ticks = 0;
	final boolean TRY_TO_RIGHT_CLICK_PROSPECTOR = true;

	/**
	 * Allows you to grab information and execute commands on every game tick
	 *
	 * @param event is the information carried in each tick
	 */
	@Subscribe
	public void onGameTick(GameTick event)
	{

		ticks++;
		if (TRY_TO_RIGHT_CLICK_PROSPECTOR && ticks % 15 == 0)
		{
			rightClickNpc(config.interactWith(), config.menuInteraction());
		}

		if (config.randomCameraMovement())
		{
			randomCameraEvent();
		}

		player = client.getLocalPlayer();

		if (config.trackPlayerMovement())
		{
			if (onPlayerLocationChanged())
			{
				Messages.sendChatMessage("Your character has moved.");
			}
		}

		// This find the canvas location of an object.
		if (config.trackNearbyObjects())
		{
			final int  WILLOW_TREE_ID = 10819;
			GameObject object         = Objects.findNearestGameObject(WILLOW_TREE_ID);
			if (object == null)
			{
				return;
			}

			minimapLocation = object.getMinimapLocation();
			if (minimapLocation != null)
			{
				Messages.sendChatMessage("nearest game object minimap information: " + minimapLocation);
				Messages.sendChatMessage("This is the distance on minimap of " + player.getMinimapLocation()
						.distanceTo(minimapLocation));
				object.getCanvasTilePoly()
						.getBounds();
			}

			LocalPoint localDestinationLocation = client.getLocalDestinationLocation();

			Messages.sendChatMessage("nearest game object information: " + object.getCanvasLocation());
			LocalPoint localLocation = object.getLocalLocation();
			String[]   actions       = object.getActions();
			for (String action :
					actions)
			{
				Messages.sendChatMessage("Options for:" + object.getName());
			}
			Messages.sendChatMessage("This is the distance of " + player.getLocalLocation()
					.distanceTo(localLocation));
			Messages.sendChatMessage("Is it on the screen?: " + Screen.isOnScreen(object));
		}


		// this finds the menu item hovered over.
		MenuRow hover = adonaiMenu.getHover();
		log.info("Hovering: {}", hover.getFullText());
	}

	/**
	 * Stores an updated copy of the menu before it the menu is loaded.
	 *
	 * @param event carries the information of the tick
	 */
	@Subscribe
	public void onBeforeMenuRender(BeforeMenuRender event)
	{
		adonaiMenu.renderAdonaiMenu();
		event.consume();
	}

	/**
	 * Gets the location of the menu items
	 *
	 * @param event holds the event information
	 */
	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{
	}

	private void removeOptionsExcept(int objectId, String exceptOption)
	{
		if (client.getGameState()
				.getState() == GameState.LOGGED_IN.getState())
		{
			MenuEntry[] clientMenuEntries = client.getMenuEntries();
			MenuEntry[] entries           = new MenuEntry[clientMenuEntries.length];
			int         added             = 0;
			for (MenuEntry e : clientMenuEntries)
			{
				if (e.getIdentifier() != objectId)
				{
					entries[added] = e;
					added++;
					continue;
				}
				if (e.getOption()
						.toLowerCase()
						.contains(exceptOption))
				{
					entries[added] = e;
					added++;
				}
			}
			MenuEntry[] finalEntries = new MenuEntry[added];
			client.setMenuEntries(Arrays.stream(entries)
					.filter(java.util.Objects::nonNull)
					.collect(Collectors.toList())
					.toArray(finalEntries));
		}
	}

	@Subscribe
	public void onFocusChanged(FocusChanged event)
	{
		if (event.isFocused() && config.randomCameraMovement())
		{
			randomCameraEvent();
		}
	}

	private void getTabInterface()
	{
		client.getWidget(TabMap.getWidget("Root Interface Container"))
				.isHidden();
	}

	private void randomCameraEvent()
	{
		if (Calculations.roll(85))
		{
			return;
		}
		executor.execute(() -> utils.robotType(Randoms.moveCamera()
				.toString()));

	}

	private boolean onPlayerLocationChanged()
	{
		if (client.getGameState()
				.getState() != GameState.LOGGED_IN.getState())
		{
			return false;
		}

		if (localLocation == null)
		{
			localLocation = player.getLocalLocation();
			return true;
		}

		LocalPoint newLocation = player.getLocalLocation();
		if (newLocation.distanceTo(localLocation) <= 0)
		{
			return false;
		}

		localLocation = newLocation;

		return true;
	}

	@Override
	protected void shutDown()
	{
		executor.shutdown();
	}

	private void logOnTick(String info, int ticks)
	{
		gameTicks++;
		if (gameTicks % ticks == 0)
		{
			log.info("Logging the HOVERED one on tick");
			log.info(info);
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{

	}
}
