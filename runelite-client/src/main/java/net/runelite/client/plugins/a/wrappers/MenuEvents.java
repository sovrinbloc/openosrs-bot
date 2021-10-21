package net.runelite.client.plugins.a.wrappers;

import net.runelite.api.TileObject;
import net.runelite.api.widgets.Menu.ContextMenu;
import net.runelite.api.widgets.Menu.MenuRow;
import net.runelite.client.plugins.a.Adonai;

public class MenuEvents
{
	private ContextMenu ctxMenu;
	private MenuRow hoveredMenuItem;

	public ContextMenu getMenu()
	{
		return ctxMenu;
	}

	public static MenuRow getHoveredMenuRow(ContextMenu ctxMenu)
	{
		return ctxMenu.getHovering(Adonai.client.getMouseCanvasPosition());
	}
}
