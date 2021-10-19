package net.runelite.api.widgets.Menu;

import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RightClickMenuHelper
{
	private final int x;
	private final int y;
	private final int h;
	private final int w;
	private String[] targets = new String[]{};
	private String[] options = new String[]{};
	private int mouseX = -1;
	private int mouseY = -1;

	public static class OptionHelper
	{
		int x, y;
		int h, w;
		int index;
		String option;
		String target;
		String fullText;
		private int opCode;
		private int arguments0;
		private int arguments1;
		private MenuEntry entry;
		private int actionId;
		private int identifier;

		private final int menuEntryId;

		private final MenuAction menuAction;
		private final java.awt.Rectangle hitBox;
		public OptionHelper(int x, int y, int w, int h, int index, String option, String target, String fullText,
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
	}

	private Map<String, OptionHelper> menuMap = new HashMap<>();

	public RightClickMenuHelper(int x, int y, int h, int w)
	{
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
	}

	public void setTargets(String[] targets)
	{
		this.targets = targets;
	}

	public void setOptions(String[] options)
	{
		this.options = options;
	}

	public void setMenuOptions(String[] options)
	{
		this.options = options;
	}

	public void setMenuOption(String options, OptionHelper helper)
	{
		this.menuMap.put(options, helper);
	}

	public void setMousePositions(int mouseX, int mouseY)
	{
		this.mouseX = mouseX;
		this.mouseY = mouseY;
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

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

	public String[] getTargets()
	{
		return targets;
	}

	public String[] getOptions()
	{
		return options;
	}

	public OptionHelper getMenuItem(String option)
	{
		if (menuMap.containsKey(option))
		{
			return menuMap.get(option);
		}
		return null;
	}

	public java.util.List<String> getKeys()
	{
		java.util.List<String> keys = new ArrayList<String>();
		menuMap.forEach((key, value) -> keys.add(key));
		return keys;
	}

	public java.util.List<OptionHelper> getMenuItems()
	{
		java.util.List<OptionHelper> values = new ArrayList<OptionHelper>();
		menuMap.forEach((key, value) -> values.add(value));
		return values;
	}
}
