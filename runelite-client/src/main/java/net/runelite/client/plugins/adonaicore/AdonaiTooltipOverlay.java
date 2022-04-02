/*
 * Copyright (c) 2017, Aria <aria@ar1as.space>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.adonaicore;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.external.adonaicore.utils.Colors;
import net.runelite.client.external.adonaicore.utils.Messages;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

import javax.inject.Inject;
import java.awt.*;
import java.util.Set;

@Slf4j
class AdonaiTooltipOverlay extends Overlay
{
	/**
	 * Menu types which are on widgets.
	 */
	private static final Set<MenuAction> WIDGET_MENU_ACTIONS = ImmutableSet.of(
			MenuAction.WIDGET_TYPE_1,
			MenuAction.WIDGET_TYPE_2,
			MenuAction.WIDGET_TYPE_3,
			MenuAction.WIDGET_TYPE_4,
			MenuAction.WIDGET_TYPE_5,
			MenuAction.WIDGET_TYPE_6,
			MenuAction.ITEM_USE_ON_WIDGET_ITEM,
			MenuAction.ITEM_USE_ON_WIDGET,
			MenuAction.ITEM_FIRST_OPTION,
			MenuAction.ITEM_SECOND_OPTION,
			MenuAction.ITEM_THIRD_OPTION,
			MenuAction.ITEM_FOURTH_OPTION,
			MenuAction.ITEM_FIFTH_OPTION,
			MenuAction.ITEM_USE,
			MenuAction.WIDGET_FIRST_OPTION,
			MenuAction.WIDGET_SECOND_OPTION,
			MenuAction.WIDGET_THIRD_OPTION,
			MenuAction.WIDGET_FOURTH_OPTION,
			MenuAction.WIDGET_FIFTH_OPTION,
			MenuAction.EXAMINE_ITEM,
			MenuAction.SPELL_CAST_ON_WIDGET,
			MenuAction.CC_OP_LOW_PRIORITY,
			MenuAction.CC_OP
	);

	private final TooltipManager tooltipManager;
	private final Client client;
	private final AdonaiConfig config;
	private final AdonaiPlugin plugin;

	@Inject
	AdonaiTooltipOverlay(Client client, AdonaiPlugin plugin, TooltipManager tooltipManager, AdonaiConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.tooltipManager = tooltipManager;
		this.config = config;
		log.info("Loading the injection");
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);

		// additionally allow tooltips above the full screen world map and welcome screen
		drawAfterInterface(WidgetID.FULLSCREEN_CONTAINER_TLI);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		MenuEntry[] menuEntries = client.getMenuEntries();
		int last = menuEntries.length - 1;
		MenuEntry menuEntry = menuEntries[last];
		String target = menuEntry.getTarget();
		String option = menuEntry.getOption();
		int objectId = menuEntry.getIdentifier();

		MenuAction type = MenuAction.of(menuEntry.getType().getId());

		if (client.isMenuOpen())
		{
			client.getAdonaiMenu();
			renderLocation(option, target, objectId);
			return null;
		}

		if (last < 0)
		{
			renderLocation("No Entries", "", -1);
			return null;
		}



		if (type == MenuAction.RUNELITE_OVERLAY || type == MenuAction.CC_OP_LOW_PRIORITY)
		{
			// These are always right click only
			renderLocation(option, target, objectId);
			return null;
		}

		if (Strings.isNullOrEmpty(option))
		{
			renderLocation(option, target, objectId);
			return null;
		}

		// Trivial options that don't need to be highlighted, add more as they appear.
		switch (option)
		{
			case "Walk here":
			case "Cancel":
			case "Continue":
				renderLocation(option, target, objectId);
				return null;
			case "Move":
				// Hide overlay on sliding puzzle boxes
				if (target.contains("Sliding piece"))
				{
					renderLocation(option, target, objectId);
					return null;
				}
		}

		if (WIDGET_MENU_ACTIONS.contains(type))
		{
			final int widgetId = menuEntry.getParam1();
			final int groupId = WidgetInfo.TO_GROUP(widgetId);

			renderLocation(option, target, objectId);
		}

		// If this varc is set, a tooltip will be displayed soon
		int tooltipTimeout = client.getVar(VarClientInt.TOOLTIP_TIMEOUT);
		if (tooltipTimeout > client.getGameCycle())
		{
			renderLocation(option, target, objectId);
			return null;
		}

		// If this varc is set, a tooltip is already being displayed
		int tooltipDisplayed = client.getVar(VarClientInt.TOOLTIP_VISIBLE);
		if (tooltipDisplayed == 1)
		{
			renderLocation(option, target, objectId);
			return null;
		}

		renderLocation(option, target, objectId);

//		String tooltipString = "";
//		String fOption = option;
//		if (!target.isBlank())
//		{
//			fOption = Messages.format("{} {}", fOption, target);
//		}
//		Point mp = client.getMouseCanvasPosition();
//		tooltipString = Messages.format("{}(ID: {}) [{}, {}]", fOption, objectId, mp.getX(), mp.getY());
//
//		tooltipManager.addFront(new Tooltip(tooltipString));

		return null;
	}

	public void renderLocation(String option, String target, int objectId)
	{
		String fOption = option;
		if (!target.isBlank())
		{
			fOption = Messages.format("{} {}", fOption, target);
		}
		if (objectId != -1 && objectId != 0)
		{
			fOption = Messages.format("{} ({})", fOption, Colors.addColorTag(objectId, Colors.Crimson));
		}
		Point mp = client.getMouseCanvasPosition();
		tooltipManager.addFront(new Tooltip(Messages.format("{} [{}, {}]", fOption, mp.getX(), mp.getY())));
	}
}
