package net.runelite.client.plugins.a;


import net.runelite.api.Player;
import net.runelite.client.config.*;
import net.runelite.client.plugins.animsmoothing.AnimationSmoothingPlugin;

import java.awt.*;

@ConfigGroup(AdonaiPlugin.CONFIG_GROUP)
public interface AdonaiConfig extends Config
{

	// Settings sections
	@ConfigSection(
			name = "MENU & GAME",
			description = "Settings for menu",
			position = 0
	)
	String menuSection = "menuSection";
	// Settings sections
	@ConfigSection(
			name = "Tracking",
			description = "Settings for tracking",
			position = 1
	)
	String trackingSection = "trackingSection";

	@ConfigSection(
			name = "NPC",
			description = "Settings for npc highlight",
			position = 2
	)
	String npcSection = "npcSection";

	@ConfigSection(
			name = "Players",
			description = "Settings for player highlight",
			position = 3
	)
	String playerSection = "playerSection";

	@ConfigSection(
			name = "Objects",
			description = "Settings for object highlight",
			position = 4
	)
	String objectSection = "objectSection";

	// npc hover options
	@ConfigItem(
			keyName = "npcShowHover",
			name = "Show on hover",
			description = "Outline NPCs when hovered",
			position = 1,
			section = npcSection
	)
	default boolean npcShowHover()
	{
		return true;
	}

	@ConfigItem(
			keyName = "objectShowHover",
			name = "Show object hover",
			description = "Outline Objects when hovered",
			position = 2,
			section = objectSection
	)
	default boolean objectShowHover()
	{
		return true;
	}

	@ConfigItem(
			keyName = "playerShowHover",
			name = "Show player hover",
			description = "Outline Players when hovered",
			position = 3,
			section = playerSection
	)
	default boolean playerShowHover()
	{
		return true;
	}


	@ConfigItem(
			keyName = "randomCameraMovement",
			name = "Random Camera Movement While Window Activated",
			description = "Configure whether or not the camera is moved, and when.",
			position = 4,
			section = menuSection
	)
	default boolean randomCameraMovement()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showMenuOptions",
			name = "Show Menu Options in Full Detail",
			description = "Configures whether or not the menu data is shown for debugging",
			position = 5,
			section = menuSection
	)
	default boolean showMenuOptions()
	{
		return true;
	}

	@ConfigItem(
			keyName = "sendToChat",
			name = "Sends debugging information to chat",
			description = "Configures whether the debugging messages are sent to chat, or only console.",
			position = 6,
			section = menuSection
	)
	default boolean sendToChat()
	{
		return true;
	}

	@ConfigItem(
			keyName = "trackPlayerMovement",
			name = "Sends debugging information about player movement",
			description = "Configures whether the to debug player movement.",
			position = 7,
			section = trackingSection
	)
	default boolean trackPlayerMovement()
	{
		return true;
	}

	@ConfigItem(
			keyName = "trackNearbyObjects",
			name = "Show debugging information about finding nearby objects",
			description = "Configures whether the to debug finding nearby objects.",
			position = 8,
			section = trackingSection
	)
	default boolean trackNearbyObjects()
	{
		return true;
	}

	@ConfigItem(
			keyName = "borderWidth",
			name = "Border Width",
			description = "Width of the outlined border",
			position = 9,
			section = menuSection
	)
	default int borderWidth()
	{
		return 4;
	}

	@ConfigItem(
			keyName = "playerBorderWidth",
			name = "Border Player Width",
			description = "Width of the outlined border player",
			position = 10,
			section = menuSection
	)
	default int playerBorderWidth()
	{
		return 4;
	}

	@Alpha
	@ConfigItem(
			keyName = "playerHoverHighlightColor",
			name = "Player hover",
			description = "The color of the hover outline for players",
			position = 11,
			section = menuSection
	)
	default Color playerHoverHighlightColor()
	{
		return new Color(0x9081006F, true);
	}

	@Alpha
	@ConfigItem(
			keyName = "objectHoverHighlightColor",
			name = "Object hover",
			description = "The color of the hover outline for objects",
			position = 12,
			section = menuSection
	)
	default Color objectHoverHighlightColor()
	{
		return new Color(0x9007D200, true);
	}

	@ConfigItem(
			keyName = "outlineFeather",
			name = "Outline feather",
			description = "Specify between 0-4 how much of the model outline should be faded",
			position = 13,
			section = menuSection
	)
	@Range(
			max = 4
	)
	default int outlineFeather()
	{
		return 4;
	}

	@Alpha
	@ConfigItem(
			keyName = "objectInteractHighlightColor",
			name = "Object interact",
			description = "The color of the target outline for objects",
			position = 14,
			section = menuSection
	)
	default Color objectInteractHighlightColor()
	{
		return new Color(0x90FF0000, true);
	}

}
