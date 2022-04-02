package net.runelite.client.external.adonai;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.widgets.menu.ContextMenu;
import net.runelite.api.widgets.menu.MenuRow;
import net.runelite.client.external.adonai.mouse.ScreenPosition;
import net.runelite.client.external.adonaicore.menu.Menus;
import net.runelite.client.external.adonaicore.objects.Objects;
import net.runelite.client.external.adonaicore.screen.Screen;
import net.runelite.client.external.adonaicore.toolbox.Calculations;
import net.runelite.client.external.adonaicore.wrappers.Menu;
import net.runelite.client.plugins.adonaicore.Adonai;

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
	}

	/**
	 * Gets a copy of the menu
	 */
	public List<MenuRow> getUpdatedMenu()
	{
		List<MenuRow> menuRows = refreshMenuOpened();

		MenuRow hNew = ctxMenu.getRowHovering(Adonai.client.getMouseCanvasPosition());

		if (hNew != null && client.isMenuOpen())
		{
			// this works
			Point screenPosition = ScreenPosition.getScreenPosition(new Point(
					hNew.getPosition().x,
					hNew.getPosition().y
			));

			// project: gets the option overed over
			logOnTick(50, "Hovering Over: (Menu Option) {} => (Menu Target) {} ({}, {})", hNew.getOption(), hNew.getTarget(), screenPosition.getX(), screenPosition.getY());
		}

//		getAllTargetTileObjects();

		// getting menu objects?
		List<TileObject> menuObjects = getMenuObjects();
		if (menuObjects == null)
		{
			return null;
		}
		for (TileObject o : menuObjects)
		{
			log.info("MenuObject: {}", o.getName());
		}

		return menuRows;

	}

	void printNewMenuItems(MenuOpened event)
	{
		ctxMenu = Adonai.client.getAdonaiMenu();
		Menu menu = new Menu(client.getAdonaiMenu(), event);
		MenuRow activeMenu = this.ctxMenu.getHovering(Calculations.convertToPoint(client.getMouseCanvasPosition()));
		for (MenuRow row : this.ctxMenu.getAllMenuRows())
		{
			log.info("row: {}, target: {}", row.getOption(), row.getTarget());
		}
	}

	public MenuRow getHover()
	{
		return this.ctxMenu.getHovering(Calculations.convertToPoint(client.getMouseCanvasPosition()));
	}

	public List<MenuRow> workingRows;

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

		workingRows = allMenuRows;
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

	/**
	 * @implNote returns a list of MenuRows
	 *
	 * @return
	 */
	public  List<MenuRow> refreshMenuOpened()
	{
		ctxMenu = client.getAdonaiMenu();
		List<MenuRow> allMenuRows = ctxMenu.getAllMenuRows();
		for (MenuRow r :
				allMenuRows)
		{
			log.info("Row full text: {}" , r.getFullText());
			Point screenPosition = ScreenPosition.getScreenPosition(new Point(
					r.getPosition().x,
					r.getPosition().y
			));

			Point hitPoint = ScreenPosition.getScreenPosition(ExtUtils.AdonaiScreen.getClickPoint(r.getHitBox()));
			log.info("Screen Position of this menu item {}: {}, {}", r.getFullText(), screenPosition.getX(),  screenPosition.getY());
			log.info("Screen Click Point of this menu item {}: {}, {}", r.getFullText(), hitPoint.getX(),  hitPoint.getY());
		}
		return allMenuRows;
	}

	public ContextMenu getAdonaiMenu()
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

	// project: contains the NPC name (this is what we need to figure out how to get).
	public  List<TileObject> getMenuObjects()
	{
		List<TileObject> tileObjects = new ArrayList<>();
		if (!client.isMenuOpen())
		{
			return null;
		}

		// project: critical aspect of finding the target (the NPC whose right click it is) [maybe the fact that the menu is open is why this is working]
		// whatis: will display the getTarget properly (doesn't show it on examine because it has nothing to do with any NPC).
		for (MenuRow menuRow : ctxMenu.getAllMenuRows())
		{
			logOnTick(50,
					"Menu data (needed to get the TileObject from the context menu): {}",
					menuRow.getMenuTargetIdentifiers());

			// project: we need to figure out how and why this works: (menuRow.getEntry()) [it contains the target, whereas other instances cannot get the target]
			logOnTick(50, "Menu entry from the previous menu row: {}", menuRow.getEntry());

			TileObject obj = Objects.getTileObject(menuRow.getMenuTargetIdentifiers());

			final int sceneX = menuRow.getEntry().getActionParam0();
			final int sceneY = menuRow.getEntry().getParam1();

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

	public NPC findNpc(int id)
	{
		return client.getCachedNPCs()[id];
	}

	/**
	 * Find the cached player.
	 *
	 * @param id the id of the player
	 */
	public Player findPlayer(int id)
	{
		return client.getCachedPlayers()[id];
	}

	/**
	 * Gets the menu and all it's options & targets, and related information and stores it.
	 */
	public void renderAdonaiMenu()
	{
		client.drawAdonaiMenu(200);
		ctxMenu = Adonai.client.getAdonaiMenu();
	}
}
