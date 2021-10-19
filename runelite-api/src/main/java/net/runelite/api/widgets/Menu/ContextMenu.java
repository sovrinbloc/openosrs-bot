package net.runelite.api.widgets.Menu;

import net.runelite.api.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContextMenu
{
	private final int x;
	private final int y;
	private final int h;
	private final int w;
	private String[] targets = new String[]{};
	private String[] options = new String[]{};
	private int mouseX = -1;
	private int mouseY = -1;

	private Map<String, MenuRow> menuMap = new HashMap<>();

	public ContextMenu(int x, int y, int h, int w)
	{
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
	}

	public void setTargets(String[] targets)
	{
		this.targets = targets;
	}

	public void setOptions(String[] options)
	{
		this.options = options;
	}

	public void setMenuOptions(String[] options)
	{
		this.options = options;
	}

	public void setMenuOption(String options, MenuRow helper)
	{
		this.menuMap.put(options, helper);
	}

	public void setMousePositions(int mouseX, int mouseY)
	{
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getH()
	{
		return h;
	}


	public int getW()
	{
		return w;
	}

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

	public String[] getTargets()
	{
		return targets;
	}

	public String[] getOptions()
	{
		return options;
	}

	public MenuRow getMenuItem(String option)
	{
		if (menuMap.containsKey(option))
		{
			return menuMap.get(option);
		}
		return null;
	}

	public java.util.List<String> getKeys()
	{
		java.util.List<String> keys = new ArrayList<String>();
		menuMap.forEach((key, value) -> keys.add(key));
		return keys;
	}

	public java.util.List<MenuRow> getMenuItems()
	{
		java.util.List<MenuRow> values = new ArrayList<MenuRow>();
		menuMap.forEach((key, value) -> values.add(value));
		return values;
	}

	public MenuRow getHovering(java.awt.Point mouse)
	{
		for (MenuRow row : getMenuItems())
		{
			Rectangle h = row.getHitBox();
			if (h.getX() < mouse.getX() && h.getX() + h.getWidth() > h.getX() &&
			h.getY() < mouse.getY() && h.getY() + h.getHeight() > h.getY())
			{
				return row;
			}
		}
		return null;
	}
}
