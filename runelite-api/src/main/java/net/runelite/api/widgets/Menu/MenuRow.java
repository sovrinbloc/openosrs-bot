package net.runelite.api.widgets.Menu;

import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;

import java.awt.*;
import java.text.MessageFormat;

public class MenuRow
{
	private int x, y;
	private int h, w;
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

	public MenuRow(int x, int y, int w, int h, int index, String option, String target, String fullText,
				   int identifier, int opCode, int arguments0, int arguments1, MenuEntry entry, int actionId,
				   int menuEntryId, MenuAction menuAction)
	{
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
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
		this.hitBox = new java.awt.Rectangle(x, y, w, h);

	}

	public int getMenuEntryId()
	{
		return menuEntryId;
	}

	public MenuAction getMenuAction()
	{
		return menuAction;
	}

	public Rectangle getHitBox()
	{
		return hitBox;
	}

	public MenuEntry getEntry()
	{
		return entry;
	}

	public void setEntry(MenuEntry entry)
	{
		this.entry = entry;
	}

	public int getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(int identifier)
	{
		this.identifier = identifier;
	}

	public int getOpCode()
	{
		return opCode;
	}

	public void setOpCode(int opCode)
	{
		this.opCode = opCode;
	}

	public int getArguments0()
	{
		return arguments0;
	}

	public void setArguments0(int arguments0)
	{
		this.arguments0 = arguments0;
	}

	public int getArguments1()
	{
		return arguments1;
	}

	public void setArguments1(int arguments1)
	{
		this.arguments1 = arguments1;
	}

	public int getActionId()
	{
		return actionId;
	}

	public void setActionId(int actionId)
	{
		this.actionId = actionId;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getH()
	{
		return h;
	}

	public int getW()
	{
		return w;
	}

	public int getIndex()
	{
		return index;
	}

	public String getOption()
	{
		return option;
	}

	public String getTarget()
	{
		return target;
	}

	public String getFullText()
	{
		return fullText;
	}

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
				this.x, this.y, this.w, this.h, this.hitBox.x, this.hitBox.y, this.hitBox.width, this.hitBox.height,
				this.index, this.option, this.target, this.fullText, this.identifier, this.opCode, this.arguments0,
				this.arguments1, this.entry, this.actionId, this.menuEntryId, this.menuAction
		);
	}
}
