package net.runelite.client.external.adonaicore.location;

import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;

public class WorldArea
{
	int x1;
	int y1;
	int x2;
	int y2;
	int center;
	int plane = 0;
	int width;
	int height;

	public WorldArea(int x1, int y1, int x2, int y2, int plane)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.plane = plane;
		organize();
	}

	public WorldArea(LocalPoint point, int radius)
	{
		this.x1 = point.getX() - radius;
		this.x2 = point.getX() + radius;
		this.y1 = point.getY() - radius;
		this.y2 = point.getY() + radius;
		organize();
	}

	public WorldArea(LocalPoint point, int radius, int plane)
	{
		this.x1 = point.getX() - radius;
		this.x2 = point.getX() + radius;
		this.y1 = point.getY() - radius;
		this.y2 = point.getY() + radius;
		this.plane = plane;
		organize();
	}

	public WorldArea(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	private void organize()
	{
		int tmp;
		if (x1 > x2)
		{
			tmp = x1;
			x1 = x2;
			x2 = tmp;
		}
		if (y1 > y2)
		{
			tmp = y1;
			y1 = y2;
			y2 = tmp;
		}
	}

	boolean contains(TileObject object)
	{
		if (!isOnPlane(object.getPlane()))
		{
			return false;
		}

		return object.getLocalLocation()
				.getX() >= x1 &&
				object.getLocalLocation()
						.getX() <= x2 &&
				object.getLocalLocation()
						.getY() >= y1 &&
				object.getLocalLocation()
						.getY() <= y2;
	}

	boolean isOnPlane(int plane)
	{
		return plane == this.plane;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getPlane()
	{
		return plane;
	}

	public int getX()
	{
		return x1;
	}

	public int getY()
	{
		return y1;
	}
}
