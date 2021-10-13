package net.runelite.client.external.adonai;

import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOpened;

import java.awt.*;

public class MenuMap
{
	MenuOpened menu;
	public static Point MenuPosition = new Point(103, 22);
	public static Point MenuDimensions = new Point(0, 0);
	public static Point MousePosition;
	public static Canvas CanvasCoordinates;

	// offset, 20 down -- 33 (12 Pt Text)
	public static final int OFFSET_DOWN = 20;
	public static final int MENU_ITEM_HEIGHT = 15;
	public static final int FONT_SIZE = 12;

	public MenuEntry[] menuItems;
	public Point mouseClicked;
	public MenuOpened menuOpened;

	public MenuMap(Client client, MenuOpened menu)
	{
		MenuEntry[] menuEntries = menu.getMenuEntries();
		// mouse is directly on top of, and in the middle of menu
		// if mouse.X > menu.getWidth() / 2  (normal menu)
		// 	then menu.x = -> ( (mouse.getX()) - (menu.getWidth() / 2) )
		//
		// if (mouse.X < menu.getWidth() / 2); (mouse geared to left)
		// 	then ( menu.x = 0 )
		//
		// if (mouse.y > canvas.Height - menu.getHeight() ) (mouse down low)
		// 	then menu.y > canvas.Height - menu.getHeight()
		//
		// if (mouse.x > (canvas.width - (menu.width() / 2))
		// 	then menu.x = canvas.width - menu.width

		this.menuOpened = menu;
		menuItems = reverse(menuEntries, menuEntries.length);
		mouseClicked = client.getMouseCanvasPosition();
		MenuPosition = new Point(client.getMenuX(), client.getMenuY());
		MousePosition = client.getMouseCanvasPosition();
		MenuDimensions = new Point(client.getMenuWidth(), client.getMenuHeight());
		CanvasCoordinates = client.getCanvas();
		getMenuPosition();
	}

	public Point getMenuPosition()
	{
		int x, y;
		if (MousePosition.getX() > (MenuDimensions.getX() / 2) &&
				MousePosition.getX() < (CanvasCoordinates.getWidth() - (MenuDimensions.getX() / 2)))
		{
			x = MousePosition.getX() + (MenuDimensions.getX() / 2);
		}
		else if (MousePosition.getX() > (CanvasCoordinates.getWidth() - (MenuDimensions.getX() / 2)))
		{
			x = CanvasCoordinates.getX() - MenuDimensions.getX();
		}
		else
		{
			x = 0;
		}

		if (MousePosition.getY() > (CanvasCoordinates.getHeight() - MenuDimensions.getY()))
		{
			y = CanvasCoordinates.getY() - MenuDimensions.getY();
		}
		else if (MousePosition.getY() <= 0)
		{
			y = 0;
		}
		else
		{
			y = MousePosition.getY();
		}
		MenuPosition = new Point(x, y);
		return MenuPosition;
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
				return new Point((int) (MenuPosition.getX() + MenuDimensions.getX() / 2.0f), (MenuPosition.getY() + OFFSET_DOWN) + (FONT_SIZE / 2) + (i * MENU_ITEM_HEIGHT));
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
			if (e.getOption().contains(name))
			{
				return new Point((int) (MenuPosition.getX() + MenuDimensions.getX() / 2.0f), (MenuPosition.getY() + OFFSET_DOWN) + (FONT_SIZE / 2) + (i * MENU_ITEM_HEIGHT));
			}
			i++;
		}
		return null;
	}

}
