package net.runelite.api.widgets.menu;

import net.runelite.api.MenuEntry;
import net.runelite.api.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextMenu
{
	private int x;
	private int y;
	private int height;
	private int width;
	private String[] targets = new String[]{};
	private String[] options = new String[]{};
	private int mouseX = -1;
	private int mouseY = -1;
	private int menuOptionCount = -1;
	private MenuEntry[] menuEntries = new MenuEntry[]{};
	private List<MenuRow> menuRows = new ArrayList<>();
	private Map<String, ArrayList<MenuRow>> menuMap = new HashMap<>();
	private Map<String, ArrayList<String>> menuTargetOptions = new HashMap<>();

	// todo: see if this works.
	public MenuRow getRowAt(Point point)
	{
		for(MenuRow row : menuRows)
		{
			if (row.getHitBox().contains(new Rectangle(point.getX(), point.getY(), 1, 1)))
			{
				return row;
			}
		}
		return null;
	}

	/**
	 * The constructor which sets all the dimensions and location of the menu.
	 *
	 * @param x      the x canvas position of the menu.
	 * @param y      the y canvas position of the menu.
	 * @param width  the width of the menu.
	 * @param height the height of the menu.
	 */
	public ContextMenu(int x, int y, int width, int height)
	{
		setMenuDimensions(x, y, width, height);
	}

	/**
	 * The constructor the set the dimensions of the menu.
	 *
	 * @param dimensions
	 */
	public ContextMenu(java.awt.Rectangle dimensions)
	{
		setMenuPosition(dimensions.x, dimensions.y);
		width = dimensions.width;
		height = dimensions.height;
	}

	/**
	 * Sets the canvas position of the menu.
	 *
	 * @param x the X position of the menu.
	 * @param y the Y position of the menu.
	 */
	public void setMenuPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Set all the menu dimensions of the context-menu.
	 *
	 * @param x      the x canvas position of the menu.
	 * @param y      the y canvas position of the menu.
	 * @param width  the width of the menu.
	 * @param height the height of the menu.
	 */
	public void setMenuDimensions(int x, int y, int width, int height)
	{
		setMenuPosition(x, y);
		this.width = width;
		this.height = height;
	}

	/**
	 * Set all the menu dimensions of the context-menu.
	 *
	 * @param x      the x canvas position of the menu.
	 * @param y      the y canvas position of the menu.
	 * @param width  the width of the menu.
	 * @param height the height of the menu.
	 */
	public void setMenuDimensions(double x, double y, double width, double height)
	{
		this.x = (int) x;
		this.y = (int) y;
		this.width = (int) width;
		this.height = (int) height;
	}

	/**
	 * Gets the dimensions and canvas position of the menu.
	 */
	public java.awt.Rectangle getDimensions()
	{
		return new java.awt.Rectangle(x, y, width, height);
	}

	/**
	 * Set the position and dimensions of the context-menu.
	 *
	 * @param dimensions the dimensions and position of the menu.
	 */
	public void setMenuDimensions(java.awt.Rectangle dimensions)
	{
		setMenuDimensions(dimensions.getX(), dimensions.getY(), dimensions.getWidth(), dimensions.getHeight());
	}

	/**
	 * Set all the targets in the right-click menu.
	 *
	 * @param targets the list of names of all the targets
	 */
	public void setTargetNames(String[] targets)
	{
		this.targets = targets;
	}

	/**
	 * Get a list of all the targets in the right-click menu.
	 */
	public String[] getTargetNames()
	{
		return targets;
	}

	/**
	 * Set the menu entries of the menu.
	 *
	 * @param entries the entries.
	 */
	public void setMenuEntries(MenuEntry[] entries)
	{
		this.menuEntries = entries;
	}

	/**
	 * Get the MenuEntries of the menu.
	 */
	public MenuEntry[] getMenuEntries()
	{
		return menuEntries;
	}

	/**
	 * Get the canvas location of the menu.
	 */
	public Point getMenuPosition()
	{
		return new Point(x, y);
	}

	/**
	 * Get the width and height of the menu
	 */
	public Point getMenuDimensions()
	{
		return new Point(width, height);
	}

	/**
	 * Get all the options names
	 */
	public String[] getOptionNames()
	{
		return options;
	}

	/**
	 * Sets the menu option names
	 *
	 * @param options the names of the options
	 */
	public void setMenuOptionNames(String[] options)
	{
		this.options = options;
	}

	/**
	 * Set all the names of the targets and options
	 *
	 * @param targets the array of the names of the targets
	 * @param options the array of the names of the options
	 */
	public void setMenuTargetOptionNames(String[] targets, String[] options)
	{
		for (int i = 0; i < targets.length; i++)
		{
			if (menuTargetOptions.containsKey(targets[i]))
			{
				ArrayList<String> opts = menuTargetOptions.get(targets[i]);
				opts.add(options[i]);
				menuTargetOptions.put(options[i], opts);
			}
			else
			{
				java.util.ArrayList<String> optionList = new ArrayList<>();
				optionList.add(options[i]);
				menuTargetOptions.put(targets[i], optionList);
			}
		}
		this.targets = targets;
		this.options = options;
	}

	/**
	 * Gets the names of the options for the target.
	 *
	 * @param target the name of the target
	 */
	public ArrayList<String> getTargetOptionsNames(String target)
	{
		return menuTargetOptions.getOrDefault(target, null);
	}

	/**
	 * Get options from the specified target
	 *
	 * @param target the name of the target
	 */
	public List<MenuRow> findTargetOptionsRows(String target)
	{
		for (MenuRow row : menuRows)
		{
			if (row.getTarget()
					.toLowerCase()
					.contains(target.toLowerCase()))
			{
				return menuMap.get(row.getTarget());
			}
		}
		return null;
	}

	/**
	 * Get the first target with the option described.
	 *
	 * @param option The option that you want to click.
	 *
	 * @return
	 */
	public MenuRow findFirstOptionRow(String option)
	{
		List<MenuRow> optionRow = findOptionRow(option);
		if (optionRow != null)
		{
			return null;
		}
		return optionRow.get(0);
	}

	/**
	 * Finds all targets which contain the option.
	 *
	 * @param option name of the right-click context option
	 */
	public List<MenuRow> findOptionRow(String option)
	{
		List<MenuRow> optionTargets = new ArrayList<>();
		for (MenuRow row : menuRows)
		{
			if (row.getOption()
					.toLowerCase()
					.contains(option.toLowerCase()))
			{
				optionTargets.add(row);
			}
		}
		return optionTargets;
	}


	/**
	 * Searches through the MenuRows to find the target and options inputted.
	 *
	 * @param target name of the target
	 * @param option name of the option
	 */
	public MenuRow findTargetOptionRow(String target, String option)
	{
		for (MenuRow row : menuRows)
		{
			if (row.getTarget()
					.toLowerCase()
					.contains(target.toLowerCase()))
			{
				if (row.getOption()
						.toLowerCase()
						.contains(option.toLowerCase()))
				{
					return row;
				}
			}
		}
		return null;
	}

	/**
	 * Adds a MenuRow to the list of specific targets.
	 *
	 * @param option the MenuRow to add to the specific option.
	 * @param row    the option Row to add to the target.
	 */
	public void addMenuRowToTarget(String option, MenuRow row)
	{
		menuRows.add(row);
		if (menuMap.containsKey(option))
		{
			ArrayList<MenuRow> menuRows = menuMap.get(option);
			menuRows.add(row);
			menuMap.put(option, menuRows);
			return;
		}
		this.menuMap.put(option, new ArrayList<>(List.of(row)));
	}

	/**
	 * Adds the MenuRow to the list of right-click options.
	 *
	 * @param row the row to add to the list
	 */
	public void addMenuRowToTarget(MenuRow row)
	{
		String option = row.getOption();
		if (menuMap.containsKey(option))
		{
			ArrayList<MenuRow> menuRows = menuMap.get(option);
			menuRows.add(row);
			menuMap.put(option, menuRows);
		}
		else
		{
			menuMap.put(row.getOption(), new ArrayList<>(List.of(row)));
		}
		this.menuMap.put(option, new ArrayList<>(List.of(row)));
	}

	/**
	 * Set all the menu option rows in the right-click menu.
	 *
	 * @param rows the list of right-click options
	 */
	public void setAllMenuRows(List<MenuRow> rows)
	{
		menuRows = rows;
		for (MenuRow row : rows)
		{
			String option = row.getOption();
			if (menuMap.containsKey(option))
			{
				ArrayList<MenuRow> menuRows = menuMap.get(option);
				menuRows.add(row);
				menuMap.put(option, menuRows);
			}
			else
			{
				menuMap.put(row.getOption(), new ArrayList<>(List.of(row)));
			}
		}
	}

	/**
	 * Get all the menu options in the right-click menu.
	 *
	 * @return
	 */
	public List<MenuRow> getAllMenuRows()
	{
		return menuRows;
	}

	/**
	 * Get the target name of the option specified
	 *
	 * @param option the name of the option / action to find the target of.
	 */
	public List<MenuRow> getMenuOptionTarget(String option)
	{
		return findOptionRow(option);
	}

	/**
	 * Set the mouse position at the time of the menu opening
	 *
	 * @param mouseX the x position of the mouse
	 * @param mouseY the y position of the mouse
	 */
	public void setMousePositions(int mouseX, int mouseY)
	{
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	/**
	 * Get the x mouse position at the time of the menu opening
	 */
	public int getMouseX()
	{
		return mouseX;
	}

	/**
	 * Get the y mouse position at the time of the menu opening
	 */
	public int getMouseY()
	{
		return mouseY;
	}

	/**
	 * Get the mouse position at the time of the menu opening.
	 */
	public Point getMousePosition()
	{
		return new Point(mouseX, mouseY);
	}

	/**
	 * Set the menu option count
	 *
	 * @param menuOptionCount the number of options
	 */
	public void setMenuOptionCount(int menuOptionCount)
	{
		this.menuOptionCount = menuOptionCount;
	}

	/**
	 * Get the count of the options in the menu
	 */
	public int getMenuOptionCount()
	{
		return menuOptionCount;
	}

	/**
	 * Get the x position of the menu on the canvas
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Get the y position of the menu on the canvas
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Get the height of the menu
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Get the width of the menu
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Get the keys of all the menu items (the Targets)
	 */
	public java.util.List<String> getKeys()
	{
		java.util.List<String> keys = new ArrayList<String>();
		menuMap.forEach((key, value) -> keys.add(key));
		return keys;
	}

	/**
	 * Get the map of all the menu items
	 */
	public java.util.Map<String, ArrayList<MenuRow>> getMenuItems()
	{
		return menuMap;
	}

	/**
	 * Gets the option over which the mouse is hovering.
	 *
	 * @param x the x position of the mouse
	 * @param y the y position of the mouse
	 */
	public MenuRow getHovering(int x, int y)
	{
		return getHovering(new java.awt.Point(x, y));
	}

	/**
	 * Gets the option over which the mouse is hovering.
	 *
	 * @param mouse position of the mouse
	 */
	public MenuRow getRowHovering(Point mouse)
	{
		return getHovering(new java.awt.Point(mouse.getX(), mouse.getY()));
	}

	/**
	 * Gets the option over which the mouse is hovering.
	 *
	 * @param mouse position of the mouse
	 */
	public MenuRow getHovering(java.awt.Point mouse)
	{
		for (MenuRow row : menuRows)
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
