package net.runelite.client.plugins.a;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Menu.ContextMenu;
import net.runelite.api.widgets.Menu.MenuRow;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.external.adonai.TabMap;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.a.objects.Objects;
import net.runelite.client.plugins.a.screen.Screen;
import net.runelite.client.plugins.a.toolbox.ScreenMath;
import net.runelite.client.plugins.a.utils.ChatMessages;
import net.runelite.client.plugins.a.wrappers.Menu;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.runelite.api.MenuAction.MENU_ACTION_DEPRIORITIZE_OFFSET;

@PluginDescriptor(
		name = "Adonai Core",
		description = "Adonai Bot Core"
)
@Slf4j
@SuppressWarnings("unused")
public class AdonaiPlugin extends Plugin
{
	static final String CONFIG_GROUP = "adonai";

	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AdonaiOverlay aOverlay;

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

	private boolean menuOpened = false;

	private boolean moveCamera = true;

	private ContextMenu ctxMenu;

	private List<String> lastMenuItems = new ArrayList<>();

	private MenuRow lastHovered = null;

	@Provides
	AdonaiConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AdonaiConfig.class);
	}

	@Override
	protected void startUp()
	{
		Adonai.client = this.client;
		Adonai.chatManager = this.chatMessageManager;
		ChatMessages.initialize(chatMessageManager);
		ChatMessages.sendChatMessage("Adonai ChatMessages Initialized.");

		executor = Executors.newFixedThreadPool(1);

		try
		{
			this.utils = new ExtUtils(client, new Keyboard());
			this.utils.setChatMessageManager(chatMessageManager);
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}

		overlayManager.add(aOverlay);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{

		if (config.randomCameraMovement())
		{
			randomCameraEvent();
		}

		player = client.getLocalPlayer();

		if (config.trackPlayerMovement())
		{
			if (isPlayerLocationChanged())
			{
				ChatMessages.sendChatMessage("Your character has moved.");
			}
		}

		if (config.trackNearbyObjects())
		{
			GameObject object = Objects.findNearestGameObject(10819);
			if (object == null)
			{
				return;
			}

			minimapLocation = object.getMinimapLocation();
			if (minimapLocation != null)
			{
				ChatMessages.sendChatMessage("nearest game object minimap information: " + minimapLocation);
				ChatMessages.sendChatMessage("This is the distance on minimap of " + player.getMinimapLocation()
						.distanceTo(minimapLocation));
				object.getCanvasTilePoly()
						.getBounds();
			}

			LocalPoint localDestinationLocation = client.getLocalDestinationLocation();

			ChatMessages.sendChatMessage("nearest game object information: " + object.getCanvasLocation());
			LocalPoint localLocation = object.getLocalLocation();
			String[] actions = object.getActions();
			for (String action :
					actions)
			{
				ChatMessages.sendChatMessage("Options for:" + object.getName());
			}
			ChatMessages.sendChatMessage("This is the distance of " + player.getLocalLocation()
					.distanceTo(localLocation));
			ChatMessages.sendChatMessage("Is it on the screen?: " + Screen.isOnScreen(object));
		}
	}

	/**
	 * gets the location of the menu items
	 *
	 * @param event
	 */
	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{

		menuOpened = true;

		ctxMenu = client.getAdonaiMenu();
		Menu menu = new Menu(client.getAdonaiMenu(), event);

		//		// not working:
		//		log.info(
		//				"Finding: {}",
		//				menu.findExactCanvasLocation("Walk")
		//						.toString()
		//		);
		//
		//		// not working:
		//		log.info(
		//				"Cancel: {}",
		//				menu.findExactCanvasLocation("Cancel")
		//						.toString()
		//		);

		MenuRow activeMenu = this.ctxMenu.getHovering(ScreenMath.convertToPoint(client.getMouseCanvasPosition()));
		for (MenuRow row : this.ctxMenu.getAllMenuRows())
		{
			log.info("row: {}, target: {}", row.getOption(), row.getTarget());
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		int type = event.getType();

		if (type >= MENU_ACTION_DEPRIORITIZE_OFFSET)
		{
			type -= MENU_ACTION_DEPRIORITIZE_OFFSET;
		}

		final MenuAction menuAction = MenuAction.of(type);

		NPC npc = client.getCachedNPCs()[event.getIdentifier()];
	}

	private int gameTicks = 0;


	@Subscribe
	public void onClientTick(ClientTick tick)
	{
		ctxMenu = client.getAdonaiMenu();
		MenuRow hNew = getHovering();

		if (hNew != null)
		{
			logOnTick(hNew.getOption(), 500);
		}

		List<String> menuList = getMenuList();
		if (lastMenuItems != null && lastHovered != null && lastMenuItems.equals(menuList) || hNew.equals(
				lastHovered))
		{
			return;
		}

		// getting menu objects?
		List<TileObject> menuObjects = getMenuObjects();
		for (TileObject o : menuObjects)
		{
			log.info("MenuObject: {}", o.getName());
		}

		refreshMenuOpened();

		lastMenuItems = menuList;
		lastHovered = hNew;
		log.info(
				"Menu Items {}",
				ctxMenu.getMenuItems()
						.size()
		);

		log.info(
				"menu items... {}",
				menuList
		);
	}

	/**
	 * Gets the current context-menu option the mouse is hovered over
	 */
	private MenuRow getHovering()
	{
		return ctxMenu.getHovering(client.getMouseCanvasPosition());
	}

	private List<TileObject> getMenuObjects()
	{
		List<TileObject> tileObjects = new ArrayList<>();

		log.info("Getting the menu rows for the entire context menu...");
		for (MenuRow r : ctxMenu.getAllMenuRows())
		{
			log.info("Menu data (needed to get the TileObject from the context menu): {}", r.getObjectData());
			log.info("Menu entry from the previous menu row: {}", r.getEntry());

			TileObject obj = Objects.findTileObject(r.getObjectData());
			if (obj != null)
			{
				if (!java.util.Objects.equals(obj.getName(), ""))
				{
					log.info(
							"obj.getName(): {}\n" +
							"obj.getCanvasLocation(): {}\n" +
							"obj.getLocalLocation(): {}\n" +
							"obj.getMinimapLocation(): {}\n" +
							"obj.getWorldLocation(): {}\n" +
							"obj.getClickbox(): {}\n" +
							"obj.getCanvasTilePoly(): {}\n",
							obj.getName(),
							obj.getCanvasLocation(),
							obj.getLocalLocation(),
							obj.getMinimapLocation(),
							obj.getWorldLocation(),
							obj.getClickbox(),
							obj.getCanvasTilePoly()
					);
				}
				tileObjects.add(obj);
			}
		}
		return tileObjects;
	}

	@Subscribe
	public void onFocusChanged(FocusChanged event)
	{
		if (event.isFocused())
		{
			moveCamera = true;
			return;
		}
		moveCamera = false;
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


	private void refreshMenuOpened()
	{
		ctxMenu = client.getAdonaiMenu();

		for (MenuRow row : ctxMenu.getAllMenuRows())
		{
			log.info("refreshed option: {}", row.getOption());
		}
	}

	@Subscribe
	public void onBeforeMenuRender(BeforeMenuRender event)
	{
		client.drawAdonaiMenu(200);
		ctxMenu = client.getAdonaiMenu();
		event.consume();
	}

	private void getTabInterface()
	{
		client.getWidget(TabMap.getWidget("Root Interface Container"))
				.isHidden();
		client.getWidget(TabMap.getWidget(""))
				.isHidden();
	}

	NPC findNpc(int id)
	{
		return client.getCachedNPCs()[id];
	}

	Player findPlayer(int id)
	{
		return client.getCachedPlayers()[id];
	}

	private static final Set<MenuAction> NPC_MENU_ACTIONS = ImmutableSet.of(
			MenuAction.NPC_FIRST_OPTION,
			MenuAction.NPC_SECOND_OPTION,
			MenuAction.NPC_THIRD_OPTION,
			MenuAction.NPC_FOURTH_OPTION,
			MenuAction.NPC_FIFTH_OPTION,
			MenuAction.SPELL_CAST_ON_NPC,
			MenuAction.ITEM_USE_ON_NPC
	);


	private void randomCameraEvent()
	{
		char cameraMovement;
		if (ExtUtils.random(1, 100) > 85)
		{
			StringBuilder outcome = new StringBuilder();
			int numberPressed = ExtUtils.random(5, 20);
			switch (ExtUtils.random(1, 5))
			{
				case 1:
					cameraMovement = Keyboard.LEFT_ARROW_KEY;
					break;
				case 2:
					cameraMovement = Keyboard.RIGHT_ARROW_KEY;
					break;
				case 3:
					cameraMovement = Keyboard.UP_ARROW_KEY;
					break;
				default:
					cameraMovement = Keyboard.DOWN_ARROW_KEY;
					break;
			}
			for (int i = 0; i < numberPressed; i++)
			{
				outcome.append(cameraMovement);
			}
			Runnable typeWords = () ->
			{

				utils.robotType(outcome.toString());
			};
			executor.execute(typeWords);
		}
	}

	private boolean isPlayerLocationChanged()
	{
		if (player == null)
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
		overlayManager.remove(aOverlay);
	}

	private static float milliToSeconds(int ms)
	{
		return ((float) ms) / 1000.0f;
	}

	private static float secondsToMillis(int s)
	{
		return ((float) s) * 1000.0f;
	}

	private List<String> getMenuList()
	{
		String list = "";
		List<String> menuList = new ArrayList<String>();
		new ArrayList<MenuEntry>(
				java.util.List.of(
						client.getMenuEntries()
				)
		).forEach(
				e -> menuList.add(e.getOption())
		);
		return menuList;
	}
}
