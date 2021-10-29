package net.runelite.client.plugins.adonaicore.wrappers;

import net.runelite.api.widgets.menu.ContextMenu;
import net.runelite.api.widgets.menu.MenuRow;
import net.runelite.client.plugins.adonaicore.Adonai;

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
		return ctxMenu.getRowHovering(Adonai.client.getMouseCanvasPosition());
	}
}
