package net.runelite.client.plugins.adonaicore.screen;

import lombok.Value;
import net.runelite.api.Point;
import net.runelite.client.plugins.adonaicore.toolbox.Calculations;

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
		return Calculations.add(point, Calculations.convertToPoint(Screen.getAbsoluteScreenLocationX0Y0()));
	}
}
