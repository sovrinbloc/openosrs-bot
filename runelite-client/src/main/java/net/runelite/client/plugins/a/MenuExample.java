package net.runelite.client.plugins.a;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.widgets.menu.ContextMenu;
import net.runelite.api.widgets.menu.MenuRow;
import net.runelite.client.plugins.a.menu.Menus;
import net.runelite.client.plugins.a.objects.Objects;
import net.runelite.client.plugins.a.toolbox.ScreenMath;
import net.runelite.client.plugins.a.wrappers.Menu;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MenuExample
{
	private Client client;

	private ContextMenu ctxMenu;

	private List<String> lastMenuItems = new ArrayList<>();

	private MenuRow lastHovered = null;
	
	private ExtUtils utils = null;

	private int gameTicks = 0;

//	@Inject
//	public MenuExample()
//	{
//		this.client = Adonai.client;
//		this.utils = new ExtUtils(client);
//		ctxMenu = client.getAdonaiMenu();
//	}
//
	@Inject
	public MenuExample(Client client)
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
			logOnTick(hNew.getOption(), 150);
		}

		List<String> menuList = getMenuList();
		if (lastMenuItems != null && lastHovered != null && lastMenuItems.equals(menuList) || hNew.equals(
				lastHovered))
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
		Menu menu = new Menu(Adonai.client.getAdonaiMenu(), event);
		MenuRow activeMenu = this.ctxMenu.getHovering(ScreenMath.convertToPoint(Adonai.client.getMouseCanvasPosition()));
		for (MenuRow row : this.ctxMenu.getAllMenuRows())
		{
			log.info("row: {}, target: {}", row.getOption(), row.getTarget());
		}
	}

	// todo: get the row location of each individual
	private void getAllTargetTileObjects()
	{
		List <TileObject> tileObjects = new ArrayList<>();
		List<MenuRow> allMenuRows = ctxMenu.getAllMenuRows();
		for (MenuRow r : allMenuRows)
		{
			MenuEntry event = r.getEntry();
			final Tile tile = Adonai.client.getScene().getTiles()[Adonai.client.getPlane()][event.getActionParam0()][event.getParam1()];
			if (tile != null)
			{
				final TileObject tileObject = Menus.findTileObject(client, tile, event.getIdentifier());
				if (tileObject != null)
				{
					log.info("tile object information {}: bounds: ({}), obj({}) pos: {}",
							tileObject.getName(),
							tileObject.getClickbox().getBounds(),
							tileObject,
							tileObject.getCanvasLocation()
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

	private void logOnTick(String info, int ticks)
	{
		gameTicks++;
		if (gameTicks % ticks == 0)
		{
			log.info("Logging the HOVERED one on tick");
			log.info(info);
		}
	}

	private List<String> getMenuList()
	{
		String list = "";
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

		log.info("Getting the menu rows for the entire context menu...");
		for (MenuRow r : ctxMenu.getAllMenuRows())
		{
			log.info("Menu data (needed to get the TileObject from the context menu): {}", r.getMenuTargetIdentifiers());
			log.info("Menu entry from the previous menu row: {}", r.getEntry());

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
							obj.getClickbox(),
							obj.getCanvasTilePoly()
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
}
