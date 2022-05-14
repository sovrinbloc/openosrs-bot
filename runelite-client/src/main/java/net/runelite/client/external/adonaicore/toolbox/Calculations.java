package net.runelite.client.external.adonaicore.toolbox;

import net.runelite.api.Point;

public class Calculations
{
	public static int random(int min, int max)
	{
		return (int) ((Math.random() * ((max - min) + 1)) + min);
	}

	/**
	 * Calculates a random number between 1 and 100 (percent), preferable used to determine
	 * if the following action will be performed.
	 * Entering a percentage of success, a random number is calculated between 1 and 100, and
	 * we compare that resulting number against (100 - chancePercentage).
	 * If result >= (100 - chancePercentage)
	 * returns true
	 * If results < (100 - chancePercentage)
	 * returns false
	 * Example:
	 * chancePercentage = 30
	 * Means that there is a 30 percent chance of the result being true.
	 *
	 * @param chancePercentage is the percentage chance of receiving a TRUE return
	 * @return random(1, 100) >= (100 - chancePercentage);
	 */
	public static boolean roll(int chancePercentage)
	{
		return Calculations.random(1, 100) >= (100 - chancePercentage);
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
