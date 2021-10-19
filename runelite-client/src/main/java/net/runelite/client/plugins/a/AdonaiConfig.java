package net.runelite.client.plugins.a;


import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins.animsmoothing.AnimationSmoothingPlugin;

@ConfigGroup(AdonaiPlugin.CONFIG_GROUP)
public interface AdonaiConfig extends Config
{

	@ConfigItem(
			keyName = "randomCameraMovement",
			name = "Random Camera Movement While Window Activated",
			description = "Configure whether or not the camera is moved, and when.",
			position = 1
	)
	default boolean randomCameraMovement()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showMenuOptions",
			name = "Show Menu Options in Full Detail",
			description = "Configures whether or not the menu data is shown for debugging",
			position = 2
	)
	default boolean showMenuOptions()
	{
		return true;
	}

	@ConfigItem(
			keyName = "sendToChat",
			name = "Sends debugging information to chat",
			description = "Configures whether the debugging messages are sent to chat, or only console.",
			position = 3
	)
	default boolean sendToChat()
	{
		return true;
	}

	@ConfigItem(
			keyName = "trackPlayerMovement",
			name = "Sends debugging information about player movement",
			description = "Configures whether the to debug player movement.",
			position = 4
	)
	default boolean trackPlayerMovement()
	{
		return true;
	}

	@ConfigItem(
			keyName = "trackNearbyObjects",
			name = "Show debugging information about finding nearby objects",
			description = "Configures whether the to debug finding nearby objects.",
			position = 5
	)
	default boolean trackNearbyObjects()
	{
		return true;
	}

}
