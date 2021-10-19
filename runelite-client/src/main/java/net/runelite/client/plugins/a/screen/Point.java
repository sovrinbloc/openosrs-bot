package net.runelite.client.plugins.a.screen;

public interface Point
{
	/**
	 * Gets the distance between this point and another.
	 *
	 * @param other other point
	 *
	 * @return the distance
	 */
	public int distanceTo(net.runelite.api.Point other);

	/**
	 * Returns a new point offset by xOff and yOff
	 *
	 * @param xOff X offset to apply
	 * @param yOff Y offset to apply
	 *
	 * @return A new instance of Point, offset by x + xOff and y + yOff
	 */
	public net.runelite.api.Point offset(int xOff, int yOff);
}
