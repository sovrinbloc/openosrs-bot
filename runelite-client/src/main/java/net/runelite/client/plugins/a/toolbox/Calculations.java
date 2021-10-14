package net.runelite.client.plugins.a.toolbox;

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
}
