package net.runelite.api.widgets.Menu;

import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;

import java.awt.*;
import java.text.MessageFormat;

public class MenuRow
{
	private int x, y;
	private int height, width;
	private int index;
	private String option;
	private String target;
	private String fullText;
	private int opCode;
	private int arguments0;
	private int arguments1;
	private MenuEntry entry;
	private int actionId;
	private int identifier;

	private final int menuEntryId;

	private final MenuAction menuAction;
	private final java.awt.Rectangle hitBox;

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
		this.hitBox = new java.awt.Rectangle(x, y, width, height);
	}

	/**
	 * Gets the menuEntryId of the right-click option.
	 */
	public int getMenuEntryId()
	{
		return menuEntryId;
	}

	/**
	 * Gets the menuOption for the right-click option.
	 */
	public MenuAction getMenuAction()
	{
		return menuAction;
	}

	/**
	 * Gets the hitbox for the right-click row.
	 */
	public Rectangle getHitBox()
	{
		return hitBox;
	}

	/**
	 * Gets the entry of the right-click option.
	 */
	public MenuEntry getEntry()
	{
		return entry;
	}

	/**
	 * Sets the entry of the right-click option.
	 *
	 * @param entry the entry for the right-click option.
	 */
	public void setEntry(MenuEntry entry)
	{
		this.entry = entry;
	}

	/**
	 * Gets the identifier of the right-click option.
	 */
	public int getIdentifier()
	{
		return identifier;
	}

	/**
	 * Sets the identifier of the right-click option.
	 *
	 * @param identifier the identifier of the right-click option.
	 */
	public void setIdentifier(int identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * Gets the OpCode of the right-click option.
	 */
	public int getOpCode()
	{
		return opCode;
	}

	/**
	 * Sets the OpCode of the right-click option
	 *
	 * @param opCode the opCode for the right-click option.
	 */
	public void setOpCode(int opCode)
	{
		this.opCode = opCode;
	}

	/**
	 * Gets Argument0 of the right-click option.
	 */
	public int getArguments0()
	{
		return arguments0;
	}

	/**
	 * Sets the argument0 of the right-click option.
	 *
	 * @param arguments0 the argument0 of the right-click option.
	 */
	public void setArguments0(int arguments0)
	{
		this.arguments0 = arguments0;
	}

	/**
	 * Gets the argument1 of the right-click option.
	 */
	public int getArguments1()
	{
		return arguments1;
	}

	/**
	 * Sets the argument1 of the right-click option.
	 *
	 * @param arguments1 the argument1 of the right-click option.
	 */
	public void setArguments1(int arguments1)
	{
		this.arguments1 = arguments1;
	}

	/**
	 * Gets the action ID of the option.
	 */
	public int getActionId()
	{
		return actionId;
	}

	/**
	 * Sets the action ID of the right-click option.
	 *
	 * @param actionId the actionID of the right-click option.
	 */
	public void setActionId(int actionId)
	{
		this.actionId = actionId;
	}

	/**
	 * Gets the X position of the row.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Gets the Y position of the row.
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Gets the relative canvas position of the menu item.
	 */
	public Point getPosition()
	{
		return new Point(x, y);
	}

	/**
	 *
	 */

	/**
	 * Gets the height of the row.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Gets the width of the row.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Gets the index of the row.
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * Gets the option of the row.
	 */
	public String getOption()
	{
		return option;
	}

	/**
	 * Gets the target of the MenuRow
	 */
	public String getTarget()
	{
		return target;
	}

	/**
	 * Gets the full text of the option and the target
	 */
	public String getFullText()
	{
		return fullText;
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
}
