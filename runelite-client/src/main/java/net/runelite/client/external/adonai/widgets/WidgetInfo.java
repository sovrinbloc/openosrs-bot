package net.runelite.client.external.adonai.widgets;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;

public enum WidgetInfo
{
	SKILLS_CONTAINER_ATTACK(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.ATTACK),
	SKILLS_CONTAINER_STRENGTH(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.STRENGTH),
	SKILLS_CONTAINER_DEFENCE(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.DEFENCE),
	SKILLS_CONTAINER_RANGED(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.RANGED),
	SKILLS_CONTAINER_PRAYER(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.PRAYER),
	SKILLS_CONTAINER_MAGIC(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.MAGIC),
	SKILLS_CONTAINER_RUNECRAFT(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.RUNECRAFT),
	SKILLS_CONTAINER_CONSTRUCTION(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.CONSTRUCTION),
	SKILLS_CONTAINER_HITPOINTS(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.HITPOINTS),
	SKILLS_CONTAINER_AGILITY(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.AGILITY),
	SKILLS_CONTAINER_HERBLORE(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.HERBLORE),
	SKILLS_CONTAINER_THIEVING(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.THIEVING),
	SKILLS_CONTAINER_CRAFTING(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.CRAFTING),
	SKILLS_CONTAINER_FLETCHING(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.FLETCHING),
	SKILLS_CONTAINER_SLAYER(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.SLAYER),
	SKILLS_CONTAINER_HUNTER(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.HUNTER),
	SKILLS_CONTAINER_MINING(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.MINING),
	SKILLS_CONTAINER_SMITHING(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.SMITHING),
	SKILLS_CONTAINER_FISHING(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.FISHING),
	SKILLS_CONTAINER_COOKING(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.COOKING),
	SKILLS_CONTAINER_FIREMAKING(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.FIREMAKING),
	SKILLS_CONTAINER_WOODCUTTING(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.WOODCUTTING),
	SKILLS_CONTAINER_FARMING(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.FARMING),
	SKILLS_CONTAINER_RUN_ENERGY(net.runelite.api.widgets.WidgetID.SKILLS_GROUP_ID, WidgetID.SkillsViewport.RUN_ENERGY);

	private final int groupId;
	private final int childId;

	WidgetInfo(int groupId, int childId)
	{
		this.groupId = groupId;
		this.childId = childId;
	}

	/**
	 * Gets the ID of the group-child pairing.
	 *
	 * @return the ID
	 */
	public int getId()
	{
		return groupId << 16 | childId;
	}

	/**
	 * Gets the group ID of the pair.
	 *
	 * @return the group ID
	 */
	public int getGroupId()
	{
		return groupId;
	}

	/**
	 * Gets the ID of the child in the group.
	 *
	 * @return the child ID
	 */
	public int getChildId()
	{
		return childId;
	}

	/**
	 * Gets the packed widget ID.
	 *
	 * @return the packed ID
	 */
	public int getPackedId()
	{
		return groupId << 16 | childId;
	}

	/**
	 * Utility method that converts an ID returned by {@link #getId()} back
	 * to its group ID.
	 *
	 * @param id passed group-child ID
	 * @return the group ID
	 */
	public static int TO_GROUP(int id)
	{
		return id >>> 16;
	}

	/**
	 * Utility method that converts an ID returned by {@link #getId()} back
	 * to its child ID.
	 *
	 * @param id passed group-child ID
	 * @return the child ID
	 */
	public static int TO_CHILD(int id)
	{
		return id & 0xFFFF;
	}

	/**
	 * Packs the group and child IDs into a single integer.
	 *
	 * @param groupId the group ID
	 * @param childId the child ID
	 * @return the packed ID
	 */
	public static int PACK(int groupId, int childId)
	{
		return groupId << 16 | childId;
	}

	public static int WIDTH(Client client)
	{
		Widget widget = client.getWidget(15, 3);
		if (widget != null)
		{
			return widget.getWidth();
		}
		return -1;
	}

	public int HEIGHT(Client client)
	{
		Widget widget = client.getWidget(groupId, childId);
		if (widget != null)
		{
			return widget.getWidth();
		}
		return -1;
	}

	public Widget WIDGET(Client client)
	{
		return client.getWidget(groupId, childId);
	}

	public Point DIMENSIONS(Client client)
	{
		return new Point(WIDTH(client), HEIGHT(client));
	}

	public Point SCREEN_COORDINATES(Client client)
	{
		Widget widget = WIDGET(client);
		return new Point(widget.getRelativeX(), widget.getRelativeY());
	}
}
