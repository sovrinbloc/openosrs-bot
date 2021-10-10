package net.runelite.client.external.adonai.mouse;

import net.runelite.api.Client;
import net.runelite.api.Point;

public class ScreenPosition
{
	public Client client;
	public ScreenPosition(Client client)
	{
		this.client = client;
	}

	public Point getScreenPosition(Point point)
	{
		java.awt.Point locationOnScreen = client.getCanvas().getLocationOnScreen();
		return new Point(point.getX() + (int)locationOnScreen.getX(), point.getY() + (int)locationOnScreen.getY());
	}
}
