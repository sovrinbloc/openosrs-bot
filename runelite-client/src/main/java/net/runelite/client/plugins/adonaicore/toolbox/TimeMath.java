package net.runelite.client.plugins.adonaicore.toolbox;

public class TimeMath
{
	private static float milliToSeconds(int ms)
	{
		return ((float) ms) / 1000.0f;
	}

	private static float secondsToMillis(int s)
	{
		return ((float) s) * 1000.0f;
	}
}
