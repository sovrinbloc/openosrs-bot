package net.runelite.client.external.adonaicore.wrappers;

import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.client.external.adonaicore.npc.NPCs;

public class MenuOption
{
	// offset, 20 down -- 33 (12 Pt Text)
	public static final int OFFSET_DOWN_Y = 20;
	public static final int MENU_ITEM_HEIGHT_MARGIN_Y = 3;
	public static final int FONT_SIZE = 12;

	protected MenuEntry entry;
	protected Point canvasLocation;
	protected int menuLocation;
	protected boolean highlighted = false;
	protected java.awt.Rectangle bounds;

	public MenuOption(MenuEntry entry, Point canvasLocation, int menuLocation)
	{
		this.entry = entry;
		this.canvasLocation = canvasLocation;
		this.menuLocation = menuLocation;
	}

	public MenuOption(MenuEntry entry, Point menuPosition, Point menuDimensions, int menuLocation)
	{
		this.entry = entry;
		int additionalOffset = MenuOption.OFFSET_DOWN_Y + menuPosition.getY() + (MenuOption.FONT_SIZE / 2);
		this.canvasLocation = new Point(
				(int) (menuPosition.getX() + menuDimensions.getX() / 2.0f),
				additionalOffset + (menuLocation * MenuOption.MENU_ITEM_HEIGHT_MARGIN_Y + MenuOption.FONT_SIZE)
		);
		this.menuLocation = menuLocation;
		this.bounds = new java.awt.Rectangle(menuPosition.getX(), canvasLocation.getY() - (MenuOption.FONT_SIZE / 2), menuDimensions.getX(), MenuOption.MENU_ITEM_HEIGHT_MARGIN_Y + MenuOption.FONT_SIZE);
		new NPCs();
	}

	private enum OUT
	{
		OUT,
		IN
	}

	public TileObject getObject()
	{
		return null;
	}

	public MenuEntry getEntry()
	{
		return entry;
	}

	public Point getExactCanvasLocation()
	{
		return canvasLocation;
	}

	public Point getRandomCanvasLocation()
	{
		return null;
	}

	public java.awt.Rectangle getRelativeBounds()
	{
		return this.bounds;
	}

	public int getMenuLocation()
	{
		return menuLocation;
	}
}
