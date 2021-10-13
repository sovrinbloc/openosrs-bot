package net.runelite.client.plugins.a.utils;

import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.events.MenuOpened;
import net.runelite.client.external.adonai.MenuMap;
import net.runelite.client.plugins.a.Adonai;
import net.runelite.client.plugins.a.toolbox.ScreenMath;

public class Coordinates
{
	public static Point getMouseCoordinates(TileObject obj)
	{
		return ScreenMath.add(obj.getCanvasLocation(), ScreenMath.convertToPoint(Adonai.client.getCanvas().getLocationOnScreen()));
	}

	public static java.awt.Rectangle getMenuBounds()
	{
		return new java.awt.Rectangle(Adonai.client.getMenuX(), Adonai.client.getMenuY(), Adonai.client.getMenuWidth(), Adonai.client.getMenuHeight());
	}

	public static MenuMap getMenu(MenuOpened menu)
	{
		return new MenuMap(Adonai.client, menu);
	}
}
