package net.runelite.client.plugins.a;

import net.runelite.api.*;
import net.runelite.client.plugins.a.objects.Objects;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.*;

public class AdonaiOverlay extends Overlay
{

	private final Client client;
	private final AdonaiPlugin plugin;
	private final AdonaiConfig config;
	private final ModelOutlineRenderer modelOutlineRenderer;

	@Inject
	private AdonaiOverlay(Client client, AdonaiPlugin plugin, AdonaiConfig config, ModelOutlineRenderer modelOutlineRenderer)
	{
		this.client = client;
		this.config = config;
		this.plugin = plugin;
		this.modelOutlineRenderer = modelOutlineRenderer;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(OverlayPriority.HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
//		renderMouseover();
		renderMenuObjects();
		return null;
	}

	private void renderMenuObjects()
	{
		MenuEntry[] menuEntries = client.getMenuEntries();
		if (menuEntries.length == 0)
		{
			return;
		}
		for (MenuEntry e : menuEntries)
		{
			renderTileObjectOutline(e);
		}
	}

	private void renderMouseover()
	{
		MenuEntry[] menuEntries = client.getMenuEntries();
		if (menuEntries.length == 0)
		{
			return;
		}

		// top item as only one are shown, they do not overlap? (maybe true)
		MenuEntry top = menuEntries[menuEntries.length - 1];
		MenuAction menuAction = MenuAction.of(top.getType());

		switch (menuAction)
		{
			case ITEM_USE_ON_GAME_OBJECT:
			case SPELL_CAST_ON_GAME_OBJECT:
			case GAME_OBJECT_FIRST_OPTION:
			case GAME_OBJECT_SECOND_OPTION:
			case GAME_OBJECT_THIRD_OPTION:
			case GAME_OBJECT_FOURTH_OPTION:
			case GAME_OBJECT_FIFTH_OPTION:
			{
				int x = top.getParam0();
				int y = top.getParam1();
				int id = top.getIdentifier();
				TileObject tileObject = Objects.findTileObject(x, y, id);
				if (tileObject != null)
				{
					modelOutlineRenderer.drawOutline(tileObject, config.borderWidth(), config.objectHoverHighlightColor(), config.outlineFeather());
				}
				break;
			}
			case ITEM_USE_ON_NPC:
			case SPELL_CAST_ON_NPC:
			case NPC_FIRST_OPTION:
			case NPC_SECOND_OPTION:
			case NPC_THIRD_OPTION:
			case NPC_FOURTH_OPTION:
			case NPC_FIFTH_OPTION:
			{
				int id = top.getIdentifier();
				NPC npc = plugin.findNpc(id);
				if (npc != null )
				{
					Color highlightColor = menuAction == MenuAction.NPC_SECOND_OPTION || menuAction == MenuAction.SPELL_CAST_ON_NPC
							? config.objectHoverHighlightColor() : new Color(0x9000FFFF);
					modelOutlineRenderer.drawOutline(npc, config.borderWidth(), highlightColor, config.outlineFeather());
				}
				break;
			}
			case PLAYER_FIRST_OPTION:
			case PLAYER_SECOND_OPTION:
			case PLAYER_THIRD_OPTION:
			case PLAYER_FOURTH_OPTION:
			case PLAYER_FIFTH_OPTION:
			{
				int id = top.getIdentifier();
				Player player = plugin.findPlayer(id);
				if (player != null )
				{
					Color highlightColor = menuAction == MenuAction.PLAYER_SECOND_OPTION || menuAction == MenuAction.SPELL_CAST_ON_PLAYER
							? config.playerHoverHighlightColor() : new Color(0x900FFF);
					modelOutlineRenderer.drawOutline(player, config.playerBorderWidth(), highlightColor, config.outlineFeather());
				}
				break;
			}
		}
	}

	private void renderTileObjectOutline(MenuEntry top)
	{
		MenuAction menuAction = MenuAction.of(top.getType());

		switch (menuAction)
		{
			case ITEM_USE_ON_GAME_OBJECT:
			case SPELL_CAST_ON_GAME_OBJECT:
			case GAME_OBJECT_FIRST_OPTION:
			case GAME_OBJECT_SECOND_OPTION:
			case GAME_OBJECT_THIRD_OPTION:
			case GAME_OBJECT_FOURTH_OPTION:
			case GAME_OBJECT_FIFTH_OPTION:
			{
				int x = top.getParam0();
				int y = top.getParam1();
				int id = top.getIdentifier();
				TileObject tileObject = Objects.findTileObject(x, y, id);
				if (tileObject != null)
				{
					modelOutlineRenderer.drawOutline(tileObject, config.borderWidth(), config.objectHoverHighlightColor(), config.outlineFeather());
				}
				break;
			}
			case ITEM_USE_ON_NPC:
			case SPELL_CAST_ON_NPC:
			case NPC_FIRST_OPTION:
			case NPC_SECOND_OPTION:
			case NPC_THIRD_OPTION:
			case NPC_FOURTH_OPTION:
			case NPC_FIFTH_OPTION:
			{
				int id = top.getIdentifier();
				NPC npc = plugin.findNpc(id);
				if (npc != null )
				{
					Color highlightColor = menuAction == MenuAction.NPC_SECOND_OPTION || menuAction == MenuAction.SPELL_CAST_ON_NPC
							? config.objectHoverHighlightColor() : new Color(0x9000FFFF);
					modelOutlineRenderer.drawOutline(npc, config.borderWidth(), highlightColor, config.outlineFeather());
				}
				break;
			}
		}
	}
}
