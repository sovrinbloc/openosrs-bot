package net.runelite.client.plugins.adonaicore;

import com.google.inject.Provides;
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
import net.runelite.client.external.adonai.TabMap;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.adonaicore.objects.Objects;
import net.runelite.client.plugins.adonaicore.screen.Screen;
import net.runelite.client.plugins.adonaicore.toolbox.Calculations;
import net.runelite.client.plugins.adonaicore.utils.ChatMessages;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private AdonaiTooltipOverlay tooltipOverlay;

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

	private List<String> lastMenuItems = new ArrayList<>();

	private MenuRow lastHovered = null;

	private int gameTicks = 0;

	private int clientTicks = 0;

	private MenuSession adonaiMenu;

	@Provides
	AdonaiConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AdonaiConfig.class);
	}

	@Override
	protected void startUp()
	{
		try
		{
			log.info("Attempting to add it");
			overlayManager.add(tooltipOverlay);
		}
		catch (Exception e)
		{
			log.info("The error is: {}", e.toString());
		}


		if (Adonai.isNullInitializeClient(client))
		{
			log.info("Initialized Adonai Client.");
		}
		if (Adonai.isNullInitializeChatMessageManager(this.chatMessageManager))
		{
			if (client.getGameState() == GameState.LOGGED_IN)
			{
				ChatMessages.sendChatMessage("Adonai ChatMessages Initialized.");
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


		executor = Executors.newFixedThreadPool(1);

		this.utils = new ExtUtils(client, Adonai.keyboard);
		this.utils.setChatMessageManager(chatMessageManager);

		overlayManager.add(aOverlay);
		adonaiMenu = new MenuSession(client);
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
			if (onPlayerLocationChanged())
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
			String[]   actions       = object.getActions();
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
	 * Gets the location of the menu items
	 *
	 * @param event holds the event information
	 */
	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{
		menuOpened = true;
		adonaiMenu.printNewMenuItems(event);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
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
		if (Calculations.random(1, 100) > 85)
		{
			StringBuilder outcome       = new StringBuilder();
			int           numberPressed = ExtUtils.random(5, 20);
			switch (Calculations.random(1, 5))
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

	private boolean onPlayerLocationChanged()
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
		overlayManager.remove(tooltipOverlay);

	}
}
