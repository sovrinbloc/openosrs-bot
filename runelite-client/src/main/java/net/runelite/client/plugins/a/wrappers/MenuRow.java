package net.runelite.client.plugins.a.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOpened;
import net.runelite.client.plugins.a.Adonai;
import net.runelite.client.plugins.a.toolbox.ScreenMath;

@Slf4j
public class MenuRow
{
	net.runelite.api.widgets.Menu.MenuRow row;

	MenuOpened menu;

	public Point mousePosition;
	public MenuRow(net.runelite.api.widgets.Menu.MenuRow row, MenuOpened menu)
	{
		this.row = row;
		this.menu = menu;
	}

	public net.runelite.api.widgets.Menu.MenuRow getRow()
	{
		return row;
	}

	public java.awt.Point getCanvasPosition()
	{
		return this.row.getPosition();
	}

	public java.awt.Point getAbsolutePosition()
	{
		return ScreenMath.add(this.row.getPosition(), ScreenMath.convertToPoint(Adonai.client.getMouseCanvasPosition()));
	}

}
