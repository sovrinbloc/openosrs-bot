package net.runelite.client.plugins.adonaicoremenu;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup(AdonaiMenuPlugin.CONFIG_GROUP)
public interface AdonaiMenuConfig extends Config
{
	// Settings sections
	@ConfigSection(
			name = "MENU & GAME",
			description = "Settings for menu",
			position = 0
	)
	String menuSection = "menuSection";

	@Alpha
	@ConfigItem(
			keyName = "canvasLocation",
			name = "Tooltip Canvas Location",
			description = "The canvas location for the mouse",
			position = 1,
			section = menuSection
	)
	default boolean tooltipLocation()
	{
		return true;
	}

}
