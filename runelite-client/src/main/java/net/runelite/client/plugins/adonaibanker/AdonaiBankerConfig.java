package net.runelite.client.plugins.adonaibanker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("banker")
public interface AdonaiBankerConfig extends Config
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
	default boolean drop()
	{
		return false;
	}

	@ConfigItem(
			keyName = "useBanker",
			name = "Use Banker",
			description = "Use banker to bank",
			section = botSettings,
			position = 3
	)
	default boolean useBanker()
	{
		return false;
	}

	@ConfigItem(
			keyName = "useBankBooth",
			name = "Use Bank Booth",
			description = "Use bank booth (Make sure to know if there is a bank booth or banker where you are going to use it)",
			section = botSettings,
			position = 4
	)
	default boolean useBankBooth()
	{
		return false;
	}

	@ConfigItem(
			keyName = "useBankDeposit",
			name = "Use Bank Deposit",
			description = "Use bank deposit booth (Make sure to know if there is a bank booth or banker where you are going to use it)",
			section = botSettings,
			position = 5
	)
	default boolean useBankDeposit()
	{
		return false;
	}

}