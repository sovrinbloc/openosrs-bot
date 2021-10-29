package net.runelite.client.plugins.adonaicore.utils;

import com.google.common.annotations.VisibleForTesting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors
{

	/**
	 * Strip color tags from a string.
	 *
	 * @param str input to strip colors from and return
	 * @return words without colors
	 */
	@VisibleForTesting
	public static String getBetweenColors(String str)
	{
		final Pattern pattern = Pattern.compile("<col=[0-9a-f]+>(.+?)</col>");
		final Matcher matcher = pattern.matcher(str);
		boolean b = matcher.find();
		if (b)
		{
			return matcher.group(1);
		}
		return str;
	}

	/**
	 * Strip color tags from a string.
	 *
	 * @param str name to
	 * @return String
	 */
	public static String stripColor(String str)
	{
		return str.replaceAll("(<col=[0-9a-f]+>|</col>)", "");
	}
}
