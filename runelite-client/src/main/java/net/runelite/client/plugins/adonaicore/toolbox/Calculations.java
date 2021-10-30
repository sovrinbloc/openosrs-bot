package net.runelite.client.plugins.adonaicore.toolbox;

import net.runelite.api.Point;

public class Calculations
{
	public static int random(int min, int max)
	{
		return (int) ((Math.random() * ((max - min) + 1)) + min);
	}
	public static int random(double min, double max)
	{
		return (int) ((Math.random() * ((max - min) + 1)) + min);
	}

	private static float milliToSeconds(int ms)
	{
		return ((float) ms) / 1000.0f;
	}

	private static float secondsToMillis(int s)
	{
		return ((float) s) * 1000.0f;
	}

	public static Point convertToPoint(java.awt.Point point)
	{
		return new Point((int) point.getX(), (int) point.getY());
	}

	public static java.awt.Point convertToPoint(Point point)
	{
		return new java.awt.Point(point.getX(), point.getY());
	}

	public static Point subtract(Point point1, Point point2)
	{
		return new Point(point1.getX() - point2.getX(), point1.getY() - point2.getY());
	}

	public static Point add(Point point1, Point point2)
	{
		return new Point(point1.getX() + point2.getX(), point1.getY() + point2.getY());
	}

	public static java.awt.Point add(java.awt.Point point1, java.awt.Point point2)
	{
		return new java.awt.Point((int) (point1.getX() + point2.getX()), (int) (point1.getY() + point2.getY()));
	}
}
