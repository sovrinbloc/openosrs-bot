package net.runelite.client.plugins.adonaitreecutter;


import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("treecutter")
public interface AdonaiTreeCutterConfig extends Config
{
	@ConfigSection(
			name = "Bot Settings",
			description = "Configuration for bot settings",
			position = 99
	)
	String botSettings = "botSection";

	@ConfigItem(
			keyName = "treeNames",
			name = "The tree names you wish to cut",
			description = "Remove menu options from random events for other players.",
			section = botSettings,
			position = 1
	)
	static String treeNames()
	{
		return "teak,mahogany";
	}

	@ConfigItem(
			keyName = "drop",
			name = "Drop Logs",
			description = "Drop the inventory of logs after it is full.",
			section = botSettings,
			position = 2
	)
	static boolean drop()
	{
		return false;
	}

}
