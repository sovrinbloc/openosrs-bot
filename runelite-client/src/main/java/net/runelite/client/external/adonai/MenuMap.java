package net.runelite.client.external.adonai;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOpened;
import net.runelite.client.plugins.a.Adonai;

import java.awt.*;

@Slf4j
public class MenuMap
{
	MenuOpened menu;
	public Point getMenuPosition = new Point(103, 22);
	public Point getMenuDimensions = new Point(0, 0);
	public Point mousePosition;
	public Canvas canvasCoordinates;

	// offset, 20 down -- 33 (12 Pt Text)
	public static final int OFFSET_DOWN = 20;
	public static final int MENU_ITEM_HEIGHT = 15;
	public static final int FONT_SIZE = 12;

	public static MenuEntry[] menuItems;
	public static Point mouseClicked;
	public static MenuOpened menuOpened;

	public MenuMap(MenuOpened menu)
	{
		initialize(menu);
	}

	private void initialize(MenuOpened menu)
	{
		MenuEntry[] menuEntries = menu.getMenuEntries();
		menuOpened = menu;
		menuItems = reverse(menuEntries, menuEntries.length);
		mouseClicked = Adonai.client.getMouseCanvasPosition();
		getMenuPosition = new Point(Adonai.client.getMenuX(), Adonai.client.getMenuY());
		mousePosition = Adonai.client.getMouseCanvasPosition();
		getMenuDimensions = new Point(Adonai.client.getMenuWidth(), Adonai.client.getMenuHeight());
		canvasCoordinates = Adonai.client.getCanvas();
		getGetMenuPosition();
	}

	public Point getGetMenuPosition()
	{
		int x, y;
		if (mousePosition.getX() > (getMenuDimensions.getX() / 2) &&
				mousePosition.getX() < (canvasCoordinates.getWidth() - (getMenuDimensions.getX() / 2)))
		{
			x = mousePosition.getX() - (getMenuDimensions.getX() / 2);
		}
		else if (mousePosition.getX() > (canvasCoordinates.getWidth() - (getMenuDimensions.getX() / 2)))
		{
			x = canvasCoordinates.getX() - getMenuDimensions.getX();
		}
		else
		{
			x = 0;
		}

		if (mousePosition.getY() > (canvasCoordinates.getHeight() - getMenuDimensions.getY()))
		{
			y = canvasCoordinates.getY() - getMenuDimensions.getY();
		}
		else if (mousePosition.getY() <= 0)
		{
			y = 0;
		}
		else
		{
			y = mousePosition.getY();
		}

		getMenuPosition = new Point(x, y);
		return getMenuPosition;
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

	public Point getMenuCanvasLocation(MenuEntry item)
	{
		int i = 0;
		for (MenuEntry e : menuItems)
		{
			if (e.getMenuAction().getId() == item.getMenuAction().getId())
			{
				return new Point(
						(int) (getMenuPosition.getX() + getMenuDimensions.getX() / 2.0f),
						(getMenuPosition.getY() + OFFSET_DOWN) + (FONT_SIZE / 2) + (i * MENU_ITEM_HEIGHT)
				);
			}
			i++;
		}
		return null;
	}

	public Point getMenuCanvasLocation(String name)
	{
		int i = 0;
		for (MenuEntry e : menuItems)
		{
			String option = e.getOption();
			if (option.contains(name) || option.toLowerCase().contains(name.toLowerCase()))
			{
				return new Point(
						(int) (getMenuPosition.getX() + getMenuDimensions.getX() / 2.0f),
						(getMenuPosition.getY() + OFFSET_DOWN) + (FONT_SIZE / 2) + (i * MENU_ITEM_HEIGHT)
				);
			}
			i++;
		}
		return null;
	}
}
