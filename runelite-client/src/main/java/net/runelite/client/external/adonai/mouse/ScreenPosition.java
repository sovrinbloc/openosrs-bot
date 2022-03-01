package net.runelite.client.external.adonai.mouse;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.external.adonai.Adonai;

public class ScreenPosition
{
	public static Client client = Adonai.Client;

	public static Point getScreenPosition(Client client, Point point)
	{
		java.awt.Point locationOnScreen = client.getCanvas().getLocationOnScreen();
		return new Point(point.getX() + (int)locationOnScreen.getX(), point.getY() + (int)locationOnScreen.getY());
	}

}
