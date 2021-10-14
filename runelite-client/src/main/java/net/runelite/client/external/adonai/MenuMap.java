package net.runelite.client.external.adonai;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOpened;
import net.runelite.client.plugins.a.Adonai;

import java.awt.*;
import java.util.ArrayList;

@Slf4j
public class MenuMap
{
	MenuOpened menu;
	public Point menuPosition = new Point(103, 22);
	public Point menuDimensions = new Point(0, 0);
	public Point mousePosition;
	public Canvas canvasCoordinates;

	public static MenuEntry[] menuItems;
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
		menuPosition = new Point(Adonai.client.getMenuX(), Adonai.client.getMenuY());
		mousePosition = Adonai.client.getMouseCanvasPosition();
		menuDimensions = new Point(Adonai.client.getMenuWidth(), Adonai.client.getMenuHeight());
		canvasCoordinates = Adonai.client.getCanvas();
		getMenuPosition();
	}

	public Point getMenuPosition()
	{
		int x, y;
		if (mousePosition.getX() > (menuDimensions.getX() / 2) &&
				mousePosition.getX() < (canvasCoordinates.getWidth() - (menuDimensions.getX() / 2)))
		{
			x = mousePosition.getX() - (menuDimensions.getX() / 2);
		}
		else if (mousePosition.getX() > (canvasCoordinates.getWidth() - (menuDimensions.getX() / 2)))
		{
			x = canvasCoordinates.getX() - menuDimensions.getX();
		}
		else
		{
			x = 0;
		}

		if (mousePosition.getY() > (canvasCoordinates.getHeight() - menuDimensions.getY()))
		{
			y = canvasCoordinates.getY() - menuDimensions.getY();
		}
		else if (mousePosition.getY() <= 0)
		{
			y = 0;
		}
		else
		{
			y = mousePosition.getY();
		}

		menuPosition = new Point(x, y);
		return menuPosition;
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

	public MenuOption getMenuOption(String name)
	{
		int i = 0;
		for (MenuEntry e : menuItems)
		{
			log.info("e.getMenuAction() {}", e.getMenuAction().toString());
			log.info("e.getTarget() {}", e.getTarget());
			String option = e.getOption();
			if (option.contains(name) || option.toLowerCase().contains(name.toLowerCase()))
			{
				return new MenuOption(e, new Point(
						(int) (menuPosition.getX() + menuDimensions.getX() / 2.0f),
						MenuOption.OFFSET_DOWN_Y + menuPosition.getY() + (MenuOption.FONT_SIZE / 2) + (i * MenuOption.MENU_ITEM_HEIGHT_MARGIN_Y + MenuOption.FONT_SIZE)
				), i);
			}
			i++;
		}
		return null;
	}

	public MenuOption getMenuOptions()
	{
		java.util.List<MenuOption> menuOptions = new ArrayList<MenuOption>();
		for (int i = 0; i < menuItems.length; i++)
		{
			menuOptions.add(new MenuOption(menuItems[i], menuPosition, menuDimensions, i));
		}
		return null;
	}

	public MenuOption getMenuOption(MenuEntry entry)
	{
		int i = 0;
		for (MenuEntry e : menuItems)
		{
			if (e.equals(entry))
			{
				return new MenuOption(e, new Point(
						(int) (menuPosition.getX() + menuDimensions.getX() / 2.0f),
						(menuPosition.getY() + MenuOption.OFFSET_DOWN_Y) + (MenuOption.FONT_SIZE / 2) + (i * MenuOption.MENU_ITEM_HEIGHT_MARGIN_Y + MenuOption.FONT_SIZE)
				), i);
			}
			i++;
		}
		return null;
	}
}
