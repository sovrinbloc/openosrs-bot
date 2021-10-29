package net.runelite.client.plugins.a.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.widgets.menu.ContextMenu;
import net.runelite.api.widgets.menu.MenuRow;
import net.runelite.client.plugins.a.Adonai;
import net.runelite.client.plugins.a.toolbox.ScreenMath;

import java.awt.*;

@Slf4j
public class Menu
{
	ContextMenu ctxMenu;

	MenuOpened menu;
	public Point mousePosition;
	public Canvas canvasCoordinates;
	public Menu(ContextMenu ctxMenu, MenuOpened menu)
	{
		this.ctxMenu = ctxMenu;
		this.menu = menu;
		initialize(ctxMenu, menu);
	}

	private void initialize(ContextMenu ctxMenu, MenuOpened menu)
	{
		mousePosition = Adonai.client.getMouseCanvasPosition();
		canvasCoordinates = Adonai.client.getCanvas();
	}

	public ContextMenu getCtxMenu()
	{
		return ctxMenu;
	}

	/* function that reverses array and stores it
       in another array*/
	private static MenuEntry[] reverse(MenuEntry[] a, int n)
	{
		MenuEntry[] b = new MenuEntry[n];
		int j = n;
		for (int i = 0; i < n; i++)
		{
			b[j - 1] = a[i];
			j = j - 1;
		}
		return b;
	}

	public MenuRow findFirstOptionRow(String option)
	{
		MenuRow op = this.ctxMenu.findFirstOptionRow(option);
		if (op == null)
		{
			return null;
		}
		return op;
	}

	public Point findExactCanvasLocation(String option)
	{
		MenuRow op = ctxMenu.findFirstOptionRow(option);
		if (op == null)
		{
			return null;
		}
		return ScreenMath.add(ScreenMath.convertToPoint(op.getPosition()), Adonai.client.getMouseCanvasPosition());
	}
}
