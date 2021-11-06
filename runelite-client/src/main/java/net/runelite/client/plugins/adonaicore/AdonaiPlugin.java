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
import net.runelite.client.external.adonai.TabMap;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.external.adonaicore.objects.Objects;
import net.runelite.client.external.adonaicore.screen.Screen;
import net.runelite.client.external.adonaicore.toolbox.Calculations;
import net.runelite.client.external.adonaicore.utils.ChatMessages;
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
		name = "Adonai Corelito Plugin",
		description = "Adonai Bot Corelito"
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

	private List<String> lastMenuItems = new ArrayList<>();

	private MenuRow lastHovered = null;

	private int gameTicks = 0;

	private int clientTicks = 0;

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

	/**
	 *  Count 1 begin
	 *  desc is the description
	 *  Count 1 end desc
	 *
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
	 * Allows you to grab information and execute commands on every game tick
	 *
	 * @param event is the information carried in each tick
	 */
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
		menuOpened = true;
//		adonaiMenu.printNewMenuItems(event);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
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
		client.getWidget(TabMap.getWidget(""))
				.isHidden();
	}

	private void randomCameraEvent()
	{
		if (Calculations.roll(85))
		{
			return;
		}
		executor.execute(() -> utils.robotType(Randoms.moveCamera().toString()));

	}

	private boolean onPlayerLocationChanged()
	{
		if (client.getGameState().getState() != GameState.LOGGED_IN.getState())
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

	private void logOnTick(String info, int ticks)
	{
		gameTicks++;
		if (gameTicks % ticks == 0)
		{
			log.info("Logging the HOVERED one on tick");
			log.info(info);
		}
	}
}
