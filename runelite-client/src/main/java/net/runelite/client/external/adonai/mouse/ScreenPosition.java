package net.runelite.client.external.adonai.mouse;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.external.adonai.Adonai;
import net.runelite.client.external.adonaicore.item.GroundItem;

public class ScreenPosition
{
	public static Client client = Adonai.Client;

	public static void initialize(Client client)
	{
		ScreenPosition.client = client;
	}

	public static Point getScreenPosition(Point point)
	{
		java.awt.Point locationOnScreen = client.getCanvas()
				.getLocationOnScreen();
		return new Point(point.getX() + (int) locationOnScreen.getX(), point.getY() + (int) locationOnScreen.getY());
	}

	public static Point getNpcScreenPoint(NPC farmer)
	{
		LocalPoint localLocation = farmer.getLocalLocation();
		Point      point         = Perspective.localToCanvas(client, localLocation, client.getPlane());
		if (point == null)
		{
			return null;
		}

		return ScreenPosition.getScreenPosition(point);
	}

	public static Point getGameObjectScreenPoint(GameObject object)
	{
		LocalPoint localLocation = object.getLocalLocation();
		Point      point         = Perspective.localToCanvas(client, localLocation, client.getPlane());
		if (point == null)
		{
			return null;
		}

		return ScreenPosition.getScreenPosition(point);
	}

	public static Point getWidgetItemScreenPoint(WidgetItem object)
	{
		Point point = object.getCanvasLocation();
		if (point == null)
		{
			return null;
		}

		return ScreenPosition.getScreenPosition(point);
	}

	public static Point getGroundItemScreenPoint(GroundItem object)
	{
		LocalPoint localLocation = LocalPoint.fromWorld(client, object.getLocation());
		Point      point         = Perspective.localToCanvas(client, localLocation, client.getPlane());
		if (point == null)
		{
			return null;
		}

		return ScreenPosition.getScreenPosition(point);
	}


}
