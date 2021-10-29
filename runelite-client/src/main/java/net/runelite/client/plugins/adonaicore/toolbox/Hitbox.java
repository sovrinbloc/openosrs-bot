package net.runelite.client.plugins.adonaicore.toolbox;

import net.runelite.api.Point;
import net.runelite.client.plugins.adonaicore.Adonai;

public class Hitbox
{
	public static Point getRandomLocation(java.awt.Rectangle hitbox)
	{
		return new Point(
				Calculations.random(hitbox.getX(), hitbox.getX() + hitbox.getWidth()),
				Calculations.random(hitbox.getY(), hitbox.getY() + hitbox.getHeight())
		);
	}

	public static Point getRandomAbsoluteLocation(java.awt.Rectangle hitbox)
	{
		Point canvasLocation = getRandomLocation(hitbox);
		return ScreenMath.add(ScreenMath.convertToPoint(Adonai.client.getCanvas().getLocationOnScreen()), canvasLocation);
	}
}
