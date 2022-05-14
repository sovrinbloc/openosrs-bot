package net.runelite.client.external.adonaicore.toolbox;

import net.runelite.api.Point;
import net.runelite.client.plugins.adonaicore.Adonai;

import java.awt.*;

public class HitBox
{
	private Rectangle hitBox;
	public HitBox(Rectangle bounds)
	{
		this.hitBox = bounds;
	}

	public HitBox(int x, int y, int width, int height)
	{
		this.hitBox = new Rectangle(x, y, width, height);
	}

	public Point getRandomLocation()
	{
		return new Point(
				Calculations.random(hitBox.getX(), hitBox.getX() + hitBox.getWidth()),
				Calculations.random(hitBox.getY(), hitBox.getY() + hitBox.getHeight())
		);
	}

	public Point getRandomAbsoluteLocation()
	{
		return Calculations.add(Calculations.convertToPoint(Adonai.client.getCanvas()
				.getLocationOnScreen()), getRandomLocation());
	}

	public boolean isMouseInMenu(Point mousePoint)
	{
		return hitBox.contains(Calculations.convertToPoint(mousePoint));
	}

}
