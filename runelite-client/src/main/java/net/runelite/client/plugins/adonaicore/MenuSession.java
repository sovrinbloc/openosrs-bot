package net.runelite.client.plugins.adonaicore;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.widgets.menu.ContextMenu;
import net.runelite.api.widgets.menu.MenuRow;
import net.runelite.client.plugins.adonaicore.menu.Menus;
import net.runelite.client.plugins.adonaicore.objects.Objects;
import net.runelite.client.plugins.adonaicore.toolbox.Calculations;
import net.runelite.client.plugins.adonaicore.toolbox.Serialize;
import net.runelite.client.plugins.adonaicore.wrappers.Menu;

import javax.inject.Inject;
import java.awt.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MenuSession
{
	private Client client;

	private ContextMenu ctxMenu;

	private List<String> lastMenuItems = new ArrayList<>();

	private MenuRow lastHovered = null;

	private ExtUtils utils = null;

	private int gameTicks = 0;

	@Inject
	public MenuSession(Client client)
	{
		assert client != null;

		if (Adonai.isNullInitializeClient(client))
		{
			log.info("Initialized Adonai Client.");
		}

		ctxMenu = client.getAdonaiMenu();

		utils = new ExtUtils(client);
	}

	public void getUpdatedMenu()
	{
		ctxMenu = Adonai.client.getAdonaiMenu();
		MenuRow hNew = ctxMenu.getRowHovering(Adonai.client.getMouseCanvasPosition());

		if (hNew != null)
		{
			// this works
			logOnTick(hNew.getOption(), 20);
		}

		List<String> menuList = getMenuList();
		if (lastMenuItems != null && lastHovered != null && lastMenuItems.equals(menuList) || java.util.Objects.equals(
				hNew,
				lastHovered
		))
		{
			return;
		}

		getAllTargetTileObjects();

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
				"menu items... {}",
				menuList
		);
	}

	void printNewMenuItems(MenuOpened event)
	{
		ctxMenu = Adonai.client.getAdonaiMenu();
		Menu    menu       = new Menu(Adonai.client.getAdonaiMenu(), event);
		MenuRow activeMenu = this.ctxMenu.getHovering(Calculations.convertToPoint(Adonai.client.getMouseCanvasPosition()));
		for (MenuRow row : this.ctxMenu.getAllMenuRows())
		{
			log.info("row: {}, target: {}", row.getOption(), row.getTarget());
		}
	}

	private List<MenuRow> currentMenuRows = new ArrayList<>();

	// todo: get the row location of each individual
	private void getAllTargetTileObjects()
	{
		List<TileObject> tileObjects = new ArrayList<>();

		for (MenuRow r : ctxMenu.getAllMenuRows())
		{
			MenuEntry event = r.getEntry();
			final Tile tile = Adonai.client.getScene()
					.getTiles()[Adonai.client.getPlane()][event.getActionParam0()][event.getParam1()];
			if (tile != null)
			{
				log.info("This Menu Row Points to {}", tile);
				log.info("-------------------------------------------");

				Menus.TileType tileObjectType = Menus.getTileObjectType(client, tile, event.getIdentifier());

				String[] actions = tile.getItemLayer()
						.getActions();

				TileObject tileObject = null;
				TileItem   itemTile   = null;
				log.info("TileObject is of type: {}", tileObjectType);
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
					//					Renderable middle = tile.getItemLayer()
					//							.getMiddle();
					//					Renderable top = tile.getItemLayer()
					//							.getTop();
					//					Renderable bottom = tile.getItemLayer()
					//							.getBottom();
					//					Point canvasLocation = tile.getItemLayer()
					//							.getCanvasLocation();
					//					int plane = tile.getPlane();
					//					SceneTileModel sceneTileModel = tile.getSceneTileModel();
					//					ItemLayer itemLayer = tile.getItemLayer();
					//					Point canvasLoc = itemLayer.getCanvasLocation();
					//					Polygon canvasTilePoly = itemLayer.getCanvasTilePoly();
					//					Model   modelBottom    = itemLayer.getModelBottom();
					//					Model   modelMiddle    = itemLayer.getModelMiddle();
					//					Model   modelTop       = itemLayer.getModelTop();
					//					String[] actions       = itemLayer.getActions();
					//					Shape clickbox = itemLayer.getClickbox();
					log.info(
							"tile object information {}: bounds: ({}), obj({}) pos: {}",
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
									.getCanvasLocation()
					);
					tileObjects.add(tileObject);

				}
			}
		}
	}

	private String actionsToString(Tile tile)
	{
		return String.join(
				", ",
				tile.getItemLayer()
						.getActions()
		);
	}


	private void refreshMenuOpened()
	{
		ctxMenu = Adonai.client.getAdonaiMenu();
	}

	private void logOnTick(String info, int ticks)
	{
		gameTicks++;
		if (gameTicks % ticks == 0)
		{
			log.info("This is the MenuRow which the mouse is hovering: {}", info);
		}
	}

	private List<String> getMenuList()
	{
		String       list     = "";
		List<String> menuList = new ArrayList<String>();
		new ArrayList<>(
				java.util.List.of(
						Adonai.client.getMenuEntries()
				)
		).forEach(
				e -> menuList.add(e.getOption())
		);
		return menuList;
	}

	private List<TileObject> getMenuObjects()
	{
		List<TileObject> tileObjects = new ArrayList<>();

		for (MenuRow r : ctxMenu.getAllMenuRows())
		{
			logOnTick(net.runelite.client.plugins.adonaicore.utils.ChatMessages.messageArgs(
					"Menu data (needed to get the TileObject from the context menu): {}",
					r.getMenuTargetIdentifiers()
			), 50);
			logOnTick(MessageFormat.format("Menu entry from the previous menu row: {}", r.getEntry()), 50);

			TileObject obj = Objects.findTileObject(r.getMenuTargetIdentifiers());
			if (obj != null)
			{
				if (!java.util.Objects.equals(obj.getName(), ""))
				{
					log.info(
							"menuObjects: name(): {}\n canvasLocation(): {}\n localLocation(): {}\n minimapLocation(): {}\n worldLocation(): {}\n clickbox(): {}\n canvasTilePoly(): {}\n",
							obj.getName(),
							obj.getCanvasLocation(),
							obj.getLocalLocation(),
							obj.getMinimapLocation(),
							obj.getWorldLocation(),
							java.util.Objects.requireNonNull(obj.getClickbox())
									.getBounds2D()
									.toString(),
							obj.getCanvasTilePoly()
									.getBounds2D()
									.toString()
					);
				}
				tileObjects.add(obj);
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
	 * Gets the current context-menu option the mouse is hovered over
	 */
	private MenuRow getHovering()
	{
		return ctxMenu.getRowHovering(Adonai.client.getMouseCanvasPosition());
	}

	void renderAdonaiMenu()
	{
		Adonai.client.drawAdonaiMenu(200);
		ctxMenu = Adonai.client.getAdonaiMenu();
	}

	MenuRow getMenuRow(Point point)
	{
		for (MenuRow row : currentMenuRows)
		{
			Rectangle hitBox = row.getHitBox();
			log.info("Row HitBox: {}", hitBox);
			if (hitBox.contains(Calculations.convertToPoint(point)))
			{
				log.info("Row {} -> {} Contains the Mouse", row.getOption(), row.getTarget());
				return row;
			}
		}
		return null;
	}
}
