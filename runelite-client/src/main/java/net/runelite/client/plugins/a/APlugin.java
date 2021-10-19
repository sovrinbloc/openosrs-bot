package net.runelite.client.plugins.a;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Menu.RightClickMenuHelper;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.a.toolbox.Calculations;
import net.runelite.client.plugins.a.wrappers.Menu;
import net.runelite.client.external.adonai.TabMap;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.a.objects.Objects;
import net.runelite.client.plugins.a.screen.Screen;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@PluginDescriptor(
		name = "A Plugin",
		description = "Random Plugin Information"
)
@Slf4j
@SuppressWarnings("unused")
public class APlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AOverlay aOverlay;

	private LocalPoint localLocation;

	private ExtUtils utils;

	@Getter
	private Point minimapLocation;

	private Player player;

	private boolean keyboard = true;


	// to execute things like key press and click -- new thread
	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);

	private ExecutorService executor;

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

		if (moveCamera)
		{
			randomCameraEvent();
		}
		player = client.getLocalPlayer();

		if (isPlayerLocationChanged())
		{
			sendChatMessage("Your character has moved.");
		}

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

	private void getTabInterface()
	{
		client.getWidget(TabMap.getWidget("Root Interface Container"))
				.isHidden();
		client.getWidget(TabMap.getWidget(""))
				.isHidden();

	}

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


	/**
	 * gets the location of the menu items
	 *
	 * @param event
	 */
	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{

		Menu menu = new Menu(event);

		menu.getMenuPosition();

		log.info("Menu Position: ({}, {})", menu.menuPosition.getX(), menu.menuPosition.getY());
		log.info("Menu W/H: ({}, {})", menu.menuDimensions.getX(), menu.menuPosition.getY());
		log.info(
				"Finding: {}",
				menu.getMenuOption("Walk")
						.getExactCanvasLocation()
						.toString()
		);
		log.info(
				"Finding: {}",
				menu.getMenuOption("Trade")
						.getExactCanvasLocation()
						.toString()
		);
		log.info(
				"Cancel: {}",
				menu.getMenuOption("Cancel")
						.getExactCanvasLocation()
						.toString()
		);
		TileObject oak = Objects.findNearestObject("Oak");
	}

	TileObject findTileObject(int x, int y, int id)
	{
		Scene scene = client.getScene();
		Tile[][][] tiles = scene.getTiles();
		Tile tile = tiles[client.getPlane()][x][y];
		if (tile != null)
		{
			for (GameObject gameObject : tile.getGameObjects())
			{
				if (gameObject != null && gameObject.getId() == id)
				{
					return gameObject;
				}
			}

			WallObject wallObject = tile.getWallObject();
			if (wallObject != null && wallObject.getId() == id)
			{
				return wallObject;
			}

			DecorativeObject decorativeObject = tile.getDecorativeObject();
			if (decorativeObject != null && decorativeObject.getId() == id)
			{
				return decorativeObject;
			}

			GroundObject groundObject = tile.getGroundObject();
			if (groundObject != null && groundObject.getId() == id)
			{
				return groundObject;
			}
		}
		return null;
	}


	double seconds = -1;
	private List<String> lastMenuItems = new ArrayList<>();
	boolean modified = false;
	float tickDiff = 0.0f;

	@Subscribe
	public void onClientTick(ClientTick tick)
	{
		RightClickMenuHelper helper = client.drawAdonaiMenu(Calculations.random(200, 255));
		float newSeconds = (float) System.currentTimeMillis();
		List<String> menuList = getMenuList();
		tickDiff = ((float) (newSeconds - seconds)) / 1000.0f;
		if (lastMenuItems.equals(menuList))
		{
			return;
		}
		// new info to share once the menu items have changed
		log.info("loop beginning ended: {}ms", seconds);
		log.info("loop ended: {}ms", newSeconds);
		log.info("difference in seconds: {}s", tickDiff);

		lastMenuItems = menuList;
		log.info("what is love? {}", helper.getMenuItems().size());
		log.info("menu items... {}", menuList);
		for (RightClickMenuHelper.OptionHelper help : helper.getMenuItems())
		{
			log.info("getFullText(): {}", help.getFullText());
			log.info("getActionId(): {}", help.getActionId());
			log.info("getArguments0() {}", help.getArguments0());
			log.info("getIdentifier() {}", help.getIdentifier());
			log.info("getOpcode() {}", help.getOpCode());
			log.info("getMenuEntryId() {}", help.getMenuEntryId());
			log.info("getArguments1() {}", help.getArguments1());
			log.info("getEntry().getParam1() {}", help.getEntry().getParam1());
			log.info("getEntry().getParam0() {}", help.getEntry().getParam0());
			log.info("getEntry().getId() {}", help.getEntry().getId());
			log.info("getEntry().getActionParam1() {}", help.getEntry().getActionParam1());
			log.info("getMenuAction().getId() {}", help.getEntry().getMenuAction().getId());
			log.info("toString() {}", help.getEntry().getMenuAction().toString());
		}
		List<NPC> cachedNPCs = client.getNpcs();
		log.info("There are {} cached NPC's.", cachedNPCs.size());
		for (NPC n : cachedNPCs)
		{
			if (n != null)
			log.info("{}", n.getCombatLevel());
		}
	}

	@Subscribe
	public void onBeforeMenuRender(BeforeMenuRender event)
	{
		RightClickMenuHelper helper = client.drawAdonaiMenu(Calculations.random(200, 255));
		event.consume();
		log.info("Beginning: ");
		if(1 == 2)
		for (RightClickMenuHelper.OptionHelper help : helper.getMenuItems())
		{
			log.info("getFullText(): {}", help.getFullText());
			log.info("getActionId(): {}", help.getActionId());
			log.info("getArguments0() {}", help.getArguments0());
			log.info("getIdentifier() {}", help.getIdentifier());
			log.info("getOpcode() {}", help.getOpCode());
			log.info("getMenuEntryId() {}", help.getMenuEntryId());
			log.info("getArguments1() {}", help.getArguments1());
			log.info("getEntry().getParam1() {}", help.getEntry().getParam1());
			log.info("getEntry().getParam0() {}", help.getEntry().getParam0());
			log.info("getEntry().getId() {}", help.getEntry().getId());
			log.info("getEntry().getActionParam1() {}", help.getEntry().getActionParam1());
			log.info("getMenuAction().getId() {}", help.getEntry().getMenuAction().getId());
			log.info("toString() {}", help.getEntry().getMenuAction().toString());
		}
		log.info("END");
	}

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
				e -> menuList.add( e.getOption())
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
