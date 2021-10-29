package net.runelite.client.plugins.adonaicore.wrappers;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Point;
import net.runelite.client.plugins.adonaicore.utils.ChatMessages;

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
		return ChatMessages.messageArgs("[{}, {}]", mouseCanvasPosition.getX(), mouseCanvasPosition.getY());
	}
}
