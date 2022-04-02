package net.runelite.client.external.adonaicore.toolbox;

public class Text
{
	public static String removeWhitespace(String text)
	{
		while (text.startsWith(" "))
		{
			text = text.substring(1);
		}
		while (text.endsWith(" "))
		{
			text = text.substring(0, text.length() - 2);
		}
		return text;
	}
}
