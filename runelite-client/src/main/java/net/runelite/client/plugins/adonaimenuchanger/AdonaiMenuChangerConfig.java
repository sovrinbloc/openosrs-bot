package net.runelite.client.plugins.adonaimenuchanger;


import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(AdonaiMenuChangerPlugin.CONFIG_GROUP)
public interface AdonaiMenuChangerConfig extends Config
{
	/**
	 * Set only specific menu items per GameObject
	 *
	 * (Menu Section)
	 */
	@ConfigSection(
			name = "Add Object Menu Settings",
			description = "Settings for Object Menu Options",
			position = 0
	)
	String objectMenuSection = "objectMenuSection";

	/**
	 * Set only specific menu items per GameObject
	 *
	 * (Menu Section)
	 */
	@ConfigSection(
			name = "Add NPC Menu Settings",
			description = "Settings for NPC Menu Options",
			position = 1
	)
	String npcMenuSelection = "npcMenuSection";

	/**
	 * Remove all of this named option from all Context Menu lists
	 *
	 * (Removal Section)
	 */
	@ConfigSection(
			name = "Removal Settings",
			description = "Generic removal settings",
			position = 2
	)
	String removalSection = "removalSection";

	/**
	 * Actually initialize, add, remove, or reset options here.
	 *
	 * (Commit Section)
	 */
	@ConfigSection(
			name = "Commit Settings",
			description = "Commit Settings",
			position = 3
	)
	String commitSettings = "commitSettings";

	/**
	 * Actually initialize, add, remove, or reset options here.
	 *
	 * (Commit Section)
	 */
	@ConfigSection(
			name = "Camera Settings",
			description = "Camera Settings",
			position = 4
	)
	String cameraSettings = "cameraSettings";

	/**
	 * Actually initialize, add, remove, or reset options here.
	 *
	 * (Commit Section)
	 */
	@ConfigSection(
			name = "Reset All",
			description = "Reset All Menu Options",
			position = 3
	)
	String resetAllSettings = "resetAllSettings";

	/**
	 * Set the Object ID (used along with ObjectOption) to customize menu entries for Object.
	 *
	 * (Menu Section)
	 */
	@ConfigItem(
			keyName = "objectId",
			name = "ObjectId to remove / add options",
			description = "The gameObject which we will be modifying",
			position = 0,
			section = objectMenuSection
	)
	default int objectId()
	{
		return 11736;
	}

	/**
	 * Set the ObjectOption Name (used along with Object ID) to customize menu entries for Object.
	 *
	 * (Menu Section)
	 */
	@ConfigItem(
			keyName = "objectOption",
			name = "Option to keep in object",
			description = "Object string to keep as option",
			position = 1,
			section = objectMenuSection
	)
	default String objectOption()
	{
		return "search for traps";
	}

	/**
	 * Set the ObjectOption Name to be removed from all Context Menus (right click menu).
	 *
	 * (Removal Section)
	 */
	@ConfigItem(
			keyName = "removeOption",
			name = "Option to remove in all objects",
			description = "Object string to remove in all options",
			position = 2,
			section = removalSection
	)
	default String removeOption()
	{
		return "examine";
	}



	/**
	 * Commit adding the option and id to the objectOptions;
	 *
	 * (Commit Section)
	 */
	@ConfigItem(
			keyName = "commitAddOption",
			name = "Add option",
			description = "Add options currently in textbox",
			position = 3,
			section = commitSettings
	)
	default boolean commitAddOption()
	{
		return false;
	}

	/**
	 * Commit adding the option and id to the objectOptions;
	 *
	 * (Commit Section)
	 */
	@ConfigItem(
			keyName = "commitAddNpcOption",
			name = "Add NPC option",
			description = "Add NPC options currently in textbox",
			position = 4,
			section = commitSettings
	)
	default boolean commitAddNpcOption()
	{
		return false;
	}

	/**
	 * Commit adding the option to be removed from all context menus
	 *
	 * (Commit Section)
	 */
	@ConfigItem(
			keyName = "commitRemoveOption",
			name = "Remove option",
			description = "Remove options currently in text box",
			position = 4,
			section = commitSettings
	)
	default boolean commitRemoveOption()
	{
		return false;
	}

	/**
	 * Reset and empty all arrays to clear all menu editing options
	 *
	 * (Commit Section)
	 */
	@ConfigItem(
			keyName = "resetOptions",
			name = "Reset / Clear All Options",
			description = "Empty & Reset Plugin of All Options",
			position = 5,
			section = resetAllSettings
	)
	default boolean resetOptions()
	{
		return false;
	}

	/**
	 * Reset and empty all arrays to clear all menu editing options
	 *
	 * (Commit Section)
	 */
	@ConfigItem(
			keyName = "pitchOption",
			name = "Change Pitch",
			description = "Change Camera Pitch",
			position = 6,
			section = cameraSettings
	)
	default int pitchOption()
	{
		return 100;
	}



	/**
	 * Commit adding the option to be removed from all context menus
	 *
	 * (Commit Section)
	 */
	@ConfigItem(
			keyName = "commitChangePitch",
			name = "Change Pitch",
			description = "Change pitch to specified",
			position = 7,
			section = cameraSettings
	)
	default boolean commitChangePitch()
	{
		return false;
	}

}
