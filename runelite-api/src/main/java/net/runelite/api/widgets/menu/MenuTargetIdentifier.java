package net.runelite.api.widgets.menu;

import lombok.Getter;
import net.runelite.api.*;

import java.text.MessageFormat;

public class MenuTargetIdentifier
{

	@Getter
	int x;

	@Getter
	int y;

	@Getter
	int id;

	@Getter
	MenuAction menuAction;

	public MenuTargetIdentifier(MenuEntry entry)
	{
		this.x = entry.getParam0();
		this.y = entry.getParam1();
		this.id = entry.getId();
		this.menuAction = MenuAction.of(entry.getType());
	}

	public String toString()
	{
		String pattern = "X, Y: ({},{})\n  Id: {}\n menuAction: {}";

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
				this.id,
				this.menuAction
		);
	}
}
