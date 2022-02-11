package net.runelite.client.plugins.adonaicore;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.widgets.menu.ContextMenu;
import net.runelite.api.widgets.menu.MenuRow;
import net.runelite.client.external.adonaicore.menu.Menus;
import net.runelite.client.external.adonaicore.objects.Objects;
import net.runelite.client.external.adonaicore.screen.Screen;
import net.runelite.client.external.adonaicore.toolbox.Calculations;
import net.runelite.client.external.adonaicore.wrappers.Menu;

import javax.inject.Inject;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MenuSession
{
	private Client client;

	private ContextMenu ctxMenu;

	private ExtUtils utils = null;

	private int gameTicks = 0;

	/**
	 * Initializes the Menu segment of the plugin
	 *
	 * @param client
	 */
	@Inject
	public MenuSession(Client client)
	{
		assert client != null;

		// initialize client.
		this.client = client;

		if (Adonai.isNullInitializeClient(client))
		{
			log.info("Initialized Adonai Client.");
		}

		ctxMenu = client.getAdonaiMenu();
		log.info("Client.getAdonaiMenu initialized: entry information... \n\t{}", ctxMenu.getMenuEntries().toString());

		utils = new ExtUtils(client);
	}

	byte[] oldContextMenu = null;
	private List<MenuRow> currentMenuRows = new ArrayList<>();

	/**
	 * Count 2 begin (before, comes Course 1)
	 * Gets a copy of the menu
	 * Count 2 end (next, comes Course 3)
	 */
	public void getUpdatedMenu()
	{
		refreshMenuOpened();

		MenuRow hNew = ctxMenu.getRowHovering(Adonai.client.getMouseCanvasPosition());

		if (hNew != null && client.isMenuOpen())
		{
			// this works
			logOnTick(50, "Hovering Over: (Menu Option) {} => (Menu Target) {}", hNew.getOption(), hNew.getTarget());
		}

		getAllTargetTileObjects();

		// getting menu objects?
		List<TileObject> menuObjects = getMenuObjects();
		for (TileObject o : menuObjects)
		{
			log.info("MenuObject: {}", o.getName());
		}

	}

	void printNewMenuItems(MenuOpened event)
	{
		ctxMenu = Adonai.client.getAdonaiMenu();
		Menu menu = new Menu(Adonai.client.getAdonaiMenu(), event);
		MenuRow activeMenu = this.ctxMenu.getHovering(Calculations.convertToPoint(Adonai.client.getMouseCanvasPosition()));
		for (MenuRow row : this.ctxMenu.getAllMenuRows())
		{
			log.info("row: {}, target: {}", row.getOption(), row.getTarget());
		}
	}

	// todo: get the row location of each individual
	private void getAllTargetTileObjects()
	{
		List<TileObject> tileObjects = new ArrayList<>();

		List<MenuRow> allMenuRows = ctxMenu.getAllMenuRows();
		int i = 0;
		for (MenuRow r : allMenuRows)
		{
			MenuEntry entry = r.getEntry();
			logOnTick(50, "{}: Entry information (for tile): ({}, {}): [{}, {}]", i, entry.getOption(), entry.getTarget(), entry.getActionParam0(), entry.getParam1());
			i++;
		}

		for (MenuRow r : allMenuRows)
		{
			MenuEntry event = r.getEntry();

			/**
			 * start: this does not work but it's supposed to get the tile location of the menu hovered
			 * todo: figure out how to get the tile object of the menu item
			 */

			// if the ID is zero, there will be no object or item in this world so return
			if (event.getIdentifier() == 0)
			{
				continue;
			}
			log.info("target: {}, option: {}, actionParam0: {}, actionParam1: {}, getParam0: {}, getParam1: {}, getId: {}, getIdentifier: {}",
					event.getTarget(), event.getOption(), event.getActionParam0(), event.getActionParam1(), event.getParam0(), event.getParam1(), event.getIdentifier(), event.getIdentifier());

			Tile tile = Adonai.client.getScene()
					.getTiles()[Adonai.client.getPlane()][event.getActionParam0()][event.getParam1()];
			log.info("Tile: {}", tile);

			/**
			 * This is the test tile...
			 * start try to get the tile location of this menu item and post the location
			 * finish try to post the world point and local point and canvas point too
			 */
			final int itemId = event.getIdentifier();
			final int sceneX = event.getActionParam0();
			final int sceneY = event.getParam1();
			final WorldPoint worldPoint = WorldPoint.fromScene(client, sceneX, sceneY, client.getPlane());
//			log.info("ItemID: {}, sceneX: {}, sceneY: {}, worldPoint: {}, {}", itemId, sceneX, sceneY, worldPoint.getX(), worldPoint.getY());
			Screen.getTileLocation(worldPoint);
			if (tile != null)
			{
				Menus.TileType tileObjectType = Menus.getTileObjectType(Adonai.client, tile, event.getIdentifier());
				log.info("TileObject is of type: {}", tileObjectType.toString());

				TileObject tileObject = null;
				TileItem itemTile = null;
				switch (tileObjectType)
				{
					case WALL_OBJECT_TYPE:
					case DECORATIVE_OBJECT_TYPE:
					case GROUND_OBJECT_TYPE:
					case TILE_ITEM_TYPE:
					case GAME_OBJECT_TYPE:
						tileObject = Menus.findTileObject(client, tile, event.getIdentifier());
						break;
					default:
						break;
				}
				if (tileObject != null)
				{
					log.info(
							"tile object information {}: bounds: ({}), obj({}) pos: {}",
							tileObject.getName(),
							tileObject.getClickbox()
									.getBounds(),
							tileObject,
							tileObject.getCanvasLocation()
					);
					tileObjects.add(tileObject);
				}
				if (itemTile != null)
				{
					Point canvasLocation = Perspective.localToCanvas(
							client,
							tile.getLocalLocation()
									.getX(),
							tile.getLocalLocation()
									.getY(),
							tile.getPlane()
					);
					String[] actions = tile.getItemLayer().getActions();
					log.info(
							"tile object information {}: bounds: ({}), obj({}) pos: {}\nThe actions are: {}",
							itemTile.getTile()
									.getItemLayer()
									.getName(),
							itemTile.getTile()
									.getItemLayer()
									.getClickbox()
									.getBounds(),
							itemTile.getTile()
									.getItemLayer(),
							itemTile.getTile()
									.getItemLayer()
									.getCanvasLocation(),
							((Object) actions).toString()
					);
					tileObjects.add(tileObject);
				}
			}
		}
	}

	private void refreshMenuOpened()
	{
		ctxMenu = Adonai.client.getAdonaiMenu();
	}

	private ContextMenu getAdonaiMenu()
	{
		return ctxMenu;
	}

	private void logOnTick(int ticks, String info, Object... arguments)
	{
		gameTicks++;
		if (gameTicks % ticks == 0)
		{
			log.info(info, arguments);
		}
	}

	private List<TileObject> getMenuObjects()
	{
		List<TileObject> tileObjects = new ArrayList<>();
		if (!client.isMenuOpen())
		{
			return null;
		}
		for (MenuRow event : ctxMenu.getAllMenuRows())
		{
			logOnTick(50,
					"Menu data (needed to get the TileObject from the context menu): {}",
					event.getMenuTargetIdentifiers());
			logOnTick(50, "Menu entry from the previous menu row: {}", event.getEntry());

			TileObject obj = Objects.getTileObject(event.getMenuTargetIdentifiers());

			final int sceneX = event.getEntry().getActionParam0();
			final int sceneY = event.getEntry().getParam1();

			if (obj != null)
			{
				log.info("Object is not null");
				if (!java.util.Objects.equals(obj.getName(), ""))
				{
					log.info("Object has a name");
					Shape clickBox = obj.getClickbox();
					Point canvasLocation = obj.getCanvasLocation();
					LocalPoint localLocation = obj.getLocalLocation();
					Point minimapLocation = obj.getMinimapLocation();
					WorldPoint worldLocation = obj.getWorldLocation();
					Polygon canvasTilePoly = obj.getCanvasTilePoly();
					Rectangle2D bounds2D = canvasTilePoly
							.getBounds2D();
					Rectangle bounds = clickBox.getBounds();
					if (bounds == null)
					{
						bounds = new Rectangle(0, 0);
					}
					String name = obj.getName();
					log.info(
							"TileObject Attributes: \n" +
									"\tmenuObjects: name(): {}\n" +
									"\t canvasLocation(): {}\n" +
									"\t localLocation(): {}\n" +
									"\t minimapLocation(): {}\n" +
									"\t worldLocation(): {}\n" +
									"\t clickbox(): {}\n" +
									"\t canvasTilePoly(): {}\n" +
									"\t bounds(): {}\n" +
									"\tbounds2d(): {}",
							name,
							canvasLocation,
							localLocation,
							minimapLocation,
							worldLocation,
							clickBox,
							canvasTilePoly,
							bounds,
							bounds2D.toString()
					);
				}
				tileObjects.add(obj);
			}
			else
			{
				List<TileItem> groundItem = Objects.getGroundItem(sceneX, sceneY);
				if (groundItem != null)
				{
					for (TileItem item :
							groundItem)
					{
						log.info("Ground Item (else): {}, {}", item.getId(), item.getQuantity());
					}
				}
			}
		}
		return tileObjects;
	}

	NPC findNpc(int id)
	{
		return Adonai.client.getCachedNPCs()[id];
	}

	/**
	 * Find the cached player.
	 *
	 * @param id the id of the player
	 */
	Player findPlayer(int id)
	{
		return Adonai.client.getCachedPlayers()[id];
	}

	/**
	 * Gets the menu and all it's options & targets, and related information and stores it.
	 */
	void renderAdonaiMenu()
	{
		Adonai.client.drawAdonaiMenu(200);
		ctxMenu = Adonai.client.getAdonaiMenu();
	}
}
