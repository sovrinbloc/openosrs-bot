package net.runelite.client.plugins.a.screen;

import lombok.Value;
import net.runelite.api.Point;
import net.runelite.client.plugins.a.toolbox.ScreenMath;

@Value
public class ScreenLocation
{
	/**
	 * Converts the screen location to canvas location.
	 *
	 * @param point
	 * @return
	 */
	public static Point fromCanvasScreenPosition(Point point)
	{
		return ScreenMath.add(point, ScreenMath.convertToPoint(Screen.getAbsoluteScreenLocationX0Y0()));
	}
}
