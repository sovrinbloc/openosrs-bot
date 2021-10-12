/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.external.adonai;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.widgets.Widget;
import net.runelite.client.external.adonai.widgets.WidgetInfo;
import net.runelite.client.plugins.a.Adonai;

import javax.annotation.Nullable;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum StatsMap
{

	ATTACK("Attack", WidgetInfo.SKILLS_CONTAINER_ATTACK),
	STRENGTH("Strength", WidgetInfo.SKILLS_CONTAINER_STRENGTH),
	DEFENCE("Defence", WidgetInfo.SKILLS_CONTAINER_DEFENCE),
	RANGED("Ranged", WidgetInfo.SKILLS_CONTAINER_RANGED),
	PRAYER("Prayer", WidgetInfo.SKILLS_CONTAINER_PRAYER),
	MAGIC("Magic", WidgetInfo.SKILLS_CONTAINER_MAGIC),
	RUNECRAFT("Runecraft", WidgetInfo.SKILLS_CONTAINER_RUNECRAFT),
	CONSTRUCTION("Construction", WidgetInfo.SKILLS_CONTAINER_CONSTRUCTION),
	HITPOINTS("Hitpoints", WidgetInfo.SKILLS_CONTAINER_HITPOINTS),
	AGILITY("Agility", WidgetInfo.SKILLS_CONTAINER_AGILITY),
	HERBLORE("Herblore", WidgetInfo.SKILLS_CONTAINER_HERBLORE),
	THIEVING("Thieving", WidgetInfo.SKILLS_CONTAINER_THIEVING),
	CRAFTING("Crafting", WidgetInfo.SKILLS_CONTAINER_CRAFTING),
	FLETCHING("Fletching", WidgetInfo.SKILLS_CONTAINER_FLETCHING),
	SLAYER("Slayer", WidgetInfo.SKILLS_CONTAINER_SLAYER),
	HUNTER("Hunter", WidgetInfo.SKILLS_CONTAINER_HUNTER),
	MINING("Mining", WidgetInfo.SKILLS_CONTAINER_MINING),
	SMITHING("Smithing", WidgetInfo.SKILLS_CONTAINER_SMITHING),
	FISHING("Fishing", WidgetInfo.SKILLS_CONTAINER_FISHING),
	COOKING("Cooking", WidgetInfo.SKILLS_CONTAINER_COOKING),
	FIREMAKING("Firemaking", WidgetInfo.SKILLS_CONTAINER_FIREMAKING),
	WOODCUTTING("Woodcutting", WidgetInfo.SKILLS_CONTAINER_WOODCUTTING),
	FARMING("Farming", WidgetInfo.SKILLS_CONTAINER_FARMING);

	private final String name;
	private final WidgetInfo info;
	private static final Map<String, WidgetInfo> map;

	static
	{
		ImmutableMap.Builder<String, WidgetInfo> builder = ImmutableMap.builder();

		for (StatsMap skills : values())
		{
			builder.put(skills.getName(), skills.getInfo());
		}

		map = builder.build();
	}

	@Nullable
	public static WidgetInfo getWidget(String skill)
	{
		return map.getOrDefault(skill, null);
	}

	public static Widget getWidgetInfo(String skill)
	{
		WidgetInfo w = map.getOrDefault(skill, null);
		return Adonai.client.getWidget(w.getGroupId(), w.getChildId());
	}
}
