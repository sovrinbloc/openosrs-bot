package net.runelite.api.widgets.menu;

import lombok.Getter;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;

import java.awt.*;
import java.text.MessageFormat;

public class MenuRow
{
	@Getter
	private final int x, y, height, width;

	@Getter
	private final int index;

	@Getter
	private final String option, target;

	@Getter
	private final String fullText;

	@Getter
	private final int opCode;

	@Getter
	private final int arguments0, arguments1;

	@Getter
	private final MenuEntry entry;

	@Getter
	private final int actionId;

	@Getter
	private final int identifier;

	@Getter
	private final int menuEntryId;

	@Getter
	private final MenuAction menuAction;

	@Getter
	private final Rectangle hitBox;

	public enum OptionType {
		ITEM_OP,
		OBJECT_OP,
		NPC_OP
	}

	@Getter
	private final OptionType optionType;

	/**
	 * The constructor for the right-click option (row).
	 *
	 * @param x           the x position of the menu row.
	 * @param y           the y position of the menu row.
	 * @param width       the width of the menu row.
	 * @param height      the height of the menu row.
	 * @param index       the index position
	 * @param option      the option name
	 * @param target      the target name
	 * @param fullText    the fulltext (option + target)
	 * @param identifier  the identifier
	 * @param opCode      the opCode
	 * @param arguments0  the arguments0
	 * @param arguments1  the arguments1
	 * @param entry       the entry number
	 * @param actionId    the actionId
	 * @param menuEntryId the menuEntryId
	 * @param menuAction  the menuAction
	 */
	public MenuRow(int x, int y, int width, int height, int index, String option, String target, String fullText,
				   int identifier, int opCode, int arguments0, int arguments1, MenuEntry entry, int actionId,
				   int menuEntryId, MenuAction menuAction)
	{
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.index = index;
		this.option = option;
		this.target = target;
		this.fullText = fullText;
		this.identifier = identifier;
		this.opCode = opCode;
		this.arguments0 = arguments0;
		this.arguments1 = arguments1;
		this.entry = entry;
		this.actionId = actionId;
		this.menuEntryId = menuEntryId;
		this.menuAction = menuAction;
		this.hitBox = new Rectangle(x, y, width, height);
		this.optionType = getOptionType(menuAction);
	}

	/**
	 * Gets the relative canvas position of the menu item.
	 */
	public Point getPosition()
	{
		return new Point(x, y);
	}

	/**
	 * Prints the menuRow to the screen in a formatted fashion.
	 */
	public String toString()
	{
		String pattern = "Menu Location: ({},{})\n Menu Width x Height: ({}, {})\n Hitbox: x,y -({}, {}), w,h - ({}, {})\n" +
				" Index: {}\n Option: {}\n Target: {}\n FullText: {}\n Identifier: {}\n opCode: {}\n Arguments0: {}\n " +
				"Arguments1: {}\n Entry: {}\n ActionId: {}\n menuEntryId: {}\n menuAction: {}";

		int i = 0;
		while (pattern.contains("{}"))
		{
			pattern = pattern.replaceFirst("\\{\\}", "{" + i + "}");
			i++;
		}
		return MessageFormat.format(
				pattern,
				this.x,
				this.y,
				this.width,
				this.height,
				this.hitBox.x,
				this.hitBox.y,
				this.hitBox.width,
				this.hitBox.height,
				this.index,
				this.option,
				this.target,
				this.fullText,
				this.identifier,
				this.opCode,
				this.arguments0,
				this.arguments1,
				this.entry,
				this.actionId,
				this.menuEntryId,
				this.menuAction
		);
	}

	/**
	 * Get the object data to be used to find the actual physical NPC or Object on the map.
	 */
	public MenuTargetIdentifier getMenuTargetIdentifiers()
	{
		return new MenuTargetIdentifier(entry);
	}


	private static OptionType getOptionType(MenuAction menuAction)
	{
		if (isItemOp(menuAction))
		{
			return OptionType.ITEM_OP;
		}
		if (isNPCOp(menuAction))
		{
			return OptionType.NPC_OP;
		}
		if (isObjectOp(menuAction))
		{
			return OptionType.OBJECT_OP;
		}
		return null;
	}

	private static boolean isItemOp(MenuAction menuAction)
	{
		final int id = menuAction.getId();
		return id >= MenuAction.ITEM_FIRST_OPTION.getId() && id <= MenuAction.ITEM_FIFTH_OPTION.getId();
	}

	private static boolean isNPCOp(MenuAction menuAction)
	{
		final int id = menuAction.getId();
		return id >= MenuAction.NPC_FIRST_OPTION.getId() && id <= MenuAction.NPC_FIFTH_OPTION.getId();
	}

	private static boolean isObjectOp(MenuAction menuAction)
	{
		final int id = menuAction.getId();
		return (id >= MenuAction.GAME_OBJECT_FIRST_OPTION.getId() && id <= MenuAction.GAME_OBJECT_FOURTH_OPTION.getId())
				|| id == MenuAction.GAME_OBJECT_FIFTH_OPTION.getId();
	}
}
