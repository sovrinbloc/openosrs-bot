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
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.external.adonai.TabMap;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.a.objects.Objects;
import net.runelite.client.plugins.a.screen.Screen;
import net.runelite.client.plugins.a.toolbox.Calculations;
import net.runelite.client.plugins.a.toolbox.ScreenMath;
import net.runelite.client.plugins.a.wrappers.Menu;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;
import java.text.MessageFormat;
import java.util.ArrayList;
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

	private boolean keyboard = true;


	// to execute things like key press and click -- new thread
	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);

	private ExecutorService executor;

	@Provides
	AdonaiConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AdonaiConfig.class);
	}

	@Override
	protected void startUp()
	{
		Adonai.client = this.client;
		executor = Executors.newFixedThreadPool(1);

		try
		{
			this.utils = new ExtUtils(client, new Keyboard());
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
				sendChatMessage("Your character has moved.");
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
				sendChatMessage("nearest game object minimap information: " + minimapLocation);
				sendChatMessage("This is the distance on minimap of " + player.getMinimapLocation()
						.distanceTo(minimapLocation));
				object.getCanvasTilePoly()
						.getBounds();
			}

			LocalPoint localDestinationLocation = client.getLocalDestinationLocation();

			sendChatMessage("nearest game object information: " + object.getCanvasLocation());
			LocalPoint localLocation = object.getLocalLocation();
			String[] actions = object.getActions();
			for (String action :
					actions)
			{
				sendChatMessage("Options for:" + object.getName());
			}
			sendChatMessage("This is the distance of " + player.getLocalLocation()
					.distanceTo(localLocation));
			sendChatMessage("Is it on the screen?: " + Screen.isOnScreen(object));
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

		ContextMenu cm = client.getAdonaiMenu();
		Menu menu = new Menu(client.getAdonaiMenu(), event);

		log.info(
				"Menu Position: ({}, {})",
				menu.getCtxMenu()
						.getMenuPosition()
						.getX(),
				menu.getCtxMenu()
						.getMenuPosition()
						.getY()
		);

		log.info("{}",
				menu.getCtxMenu()
						.getMenuItems()
						.toString()
		);
		log.info(
				"Menu W/H: ({}, {})",
				menu.getCtxMenu()
						.getDimensions()
						.getX(),
				menu.getCtxMenu()
						.getDimensions()
						.getY()
		);
		log.info(
				"Finding: {}",
				menu.findExactCanvasLocation("Walk")
						.toString()
		);

		log.info(
				"Cancel: {}",
				menu.findExactCanvasLocation("Cancel")
						.toString()
		);

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

	@Subscribe
	public void onClientTick(ClientTick tick)
	{
		ctxMenu = client.getAdonaiMenu();
		for (MenuRow row : ctxMenu.getAllMenuRows())
		{
			log.info("option: {}", row.getOption());
		}

		float newSeconds = (float) System.currentTimeMillis();
		List<String> menuList = getMenuList();
		tickDiff = ((float) (newSeconds - seconds)) / 1000.0f;

		MenuRow hovering = ctxMenu.getHovering(ScreenMath.convertToPoint(client.getMouseCanvasPosition()));
		if (lastMenuItems != null && lastHovered != null && lastMenuItems.equals(menuList) || hovering.equals(
				lastHovered))
		{
			return;
		}

		log.info("Hovering: {}", hovering.toString());
		log.info("Hovering over: {} with target: {}", hovering.getOption(), hovering.getTarget());

		lastMenuItems = menuList;
		lastHovered = hovering;
		log.info(
				"Menu Items {}",
				ctxMenu.getMenuItems()
						.size()
		);

		log.info(
				"menu items... {}",
				menuList
		);
		client.getLocalPlayer().getInteracting().getName();
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

	private boolean menuOpened = false;

	private String menuHash = "";
	double seconds = -1;
	private List<String> lastMenuItems = new ArrayList<>();
	ContextMenu ctxMenu;
	private MenuRow lastHovered = null;
	boolean modified = false;

	float tickDiff = 0.0f;


	private boolean moveCamera = true;

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

	private void sendChatMessage(String pattern, Object... arguments)
	{
		sendChatMessage(MessageFormat.format(pattern, arguments));
	}

	private String messageArgs(String pattern, Object... arguments)
	{
		return MessageFormat.format(pattern, arguments);
	}

	private QueuedMessage toChatMessage(String pattern, Object... arguments)
	{
		return QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage(MessageFormat.format(pattern, arguments))
				.build();
	}

	public void sendChatMessage(QueuedMessage message)
	{
		chatMessageManager.queue(message);
	}

	private void sendChatMessage(String chatMessage)
	{
		final String message = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage)
				.build();

		chatMessageManager.queue(
				QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(message)
						.build());
	}
}
