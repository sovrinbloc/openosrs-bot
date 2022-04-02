package net.runelite.client.external.adonaicore.wrappers;

import net.runelite.api.Point;
import net.runelite.client.external.adonaicore.utils.Messages;

public class MouseSession
{
	public static Point getMouseCanvasPosition()
	{
		return mouseCanvasPosition;
	}

	public static String setMouseCanvasPosition(Point mouseCanvasPosition)
	{
		MouseSession.mouseCanvasPosition = mouseCanvasPosition;
		return toBracketString();
	}

	private static Point mouseCanvasPosition = new Point(0, 0);
	public static String toBracketString()
	{
		return Messages.format("[{}, {}]", mouseCanvasPosition.getX(), mouseCanvasPosition.getY());
	}
}
