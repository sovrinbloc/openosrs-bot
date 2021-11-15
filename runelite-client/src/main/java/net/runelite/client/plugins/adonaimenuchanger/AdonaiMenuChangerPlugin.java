package net.runelite.client.plugins.adonaimenuchanger;

import com.google.inject.Provides;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.external.adonaicore.objects.Objects;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@PluginDescriptor(
		name = "Adonai Menu Changer Plugin",
		description = "Adonai Menu Changer Plugin"
)
@Slf4j
@SuppressWarnings("unused")
public class AdonaiMenuChangerPlugin extends Plugin
{
	static final String CONFIG_GROUP = "adonaimenuchanger";

	@Provides
	AdonaiMenuChangerConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AdonaiMenuChangerConfig.class);
	}


	@Inject
	private Client client;

	@Inject
	private AdonaiMenuChangerConfig config;

	final private Map<Integer, List<String>> objectOptions = new HashMap<>();
	final private Map<Integer, List<String>> npcOptions = new HashMap<>();
	final private List<String> removeOptions = new ArrayList<>();

	final public static String MENU_OPTION_SEARCH_FOR_TRAPS = "search for traps";
	final public static String MENU_OPTION_EXAMINE = "examine";
	final public static int OBJECT_ID_NATURE_CHEST = 11736;

	@Override
	protected void startUp()
	{
		modifyObjectMenu(OBJECT_ID_NATURE_CHEST, MENU_OPTION_SEARCH_FOR_TRAPS);
		removeFromAllMenus(MENU_OPTION_EXAMINE);
	}


	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(CONFIG_GROUP))
		{
			if (config.resetOptions())
			{
				objectOptions.clear();
				npcOptions.clear();
				return;
			}

			if (config.commitAddOption())
			{
				modifyObjectMenu(config.objectId(), config.objectOption());
				return;
			}

			if (config.commitAddNpcOption())
			{
				modifyNPCMenu(config.objectId(), config.objectOption());
				return;
			}

			if (config.commitRemoveOption())
			{
				String[] removed = new String[removeOptions.size()];
				removeFromAllMenus(config.removeOption());
				log.info("Items left in options: {}", Strings.join(removeOptions.toArray(removed), ", "));
				return;
			}

			if (config.commitChangePitch())
			{
				client.setCameraPitchTarget(config.pitchOption());
			}
		}
	}

	public void modifyObjectMenu(int objectId, String menuOption)
	{
		onlyShowOnMenu(objectOptions, objectId, menuOption);
	}


	public void modifyNPCMenu(int objectId, String menuOption)
	{
		onlyShowOnMenu(npcOptions, objectId, menuOption);
	}

	public void onlyShowOnMenu(Map<Integer, List<String>> objectOptions, int objectId, String menuOption)
	{
		String lwrMenuOption = menuOption.toLowerCase();
		if (objectOptions.containsKey(objectId))
		{
			if (!objectOptions.get(objectId).contains(lwrMenuOption))
			{
				objectOptions.get(objectId).add(lwrMenuOption);
			}
			return;
		}
		objectOptions.put(objectId, new ArrayList<>(Arrays.asList(lwrMenuOption)));
	}

	public void removeFromAllMenus(String newMenuOption)
	{
		if (removeOptions.contains(newMenuOption.toLowerCase()))
		{
			log.info("Did not have to add, because it already existed... \"{}\"", newMenuOption.toLowerCase());
			log.info("\tNumber of items in removeOptions: {}", removeOptions.size());
			return;
		}
		removeOptions.add(newMenuOption.toLowerCase());
		log.info("Added Menu Option: \"{}\" to REMOVE List", newMenuOption.toLowerCase());
		log.info("\tNumber of items in removeOptions: {}", removeOptions.size());
	}


	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded entry)
	{
		MenuEntry[] menuEntries = client.getMenuEntries();
		MenuAction menuAction = MenuAction.of(entry.getType());

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
				int x = entry.getParam0();
				int y = entry.getParam1();
				int id = entry.getIdentifier();
				TileObject tileObject = Objects.getTileObject(x, y, id);
				if (tileObject != null)
				{
					setMenuOptions();
				}
				break;
			}
			case ITEM_USE_ON_NPC:
			case SPELL_CAST_ON_NPC:

			// what: this is any of the npc options. when hovered over, the npc is highlighted.
			case NPC_FIRST_OPTION:
			case NPC_SECOND_OPTION:
			case NPC_THIRD_OPTION:
			case NPC_FOURTH_OPTION:
			case NPC_FIFTH_OPTION:
			case EXAMINE_OBJECT:
			case EXAMINE_ITEM:
			case EXAMINE_ITEM_GROUND:
			case EXAMINE_NPC:
			{
				setMenuOptions();
				break;
			}
		}

	}

	/**
	 * Usage A: Traverses through a map: of Map<Integer (id), String (option)menu >().
	 * Also traverses through MenuEntry[]. If the *id*, and *option*, both exist within
	 * the menu as well, then we will keep that entry, and delete all other entries.
	 * <p>
	 * Usage B: Traverses through the Map of Map<GameObject Id, MenuOptions (String)>().
	 * Checks if the object in the menu itself belongs to an object in which is
	 * in the list of MenuRemovals. If so, remove all items except those named.
	 */
	private void setMenuOptions()
	{
		if (client.getGameState().getState() == GameState.LOGGED_IN.getState())
		{
			MenuEntry[] clientMenuEntries = client.getMenuEntries();
			MenuEntry[] entries = new MenuEntry[clientMenuEntries.length];

			int added = 0;

			for (MenuEntry entry : clientMenuEntries)
			{
				boolean addedEntry = false;

				if (!objectOptions.containsKey(entry.getId()))
				{
					entries[added] = entry;
					added++;
					continue;
				}

				for (String exceptOption : objectOptions.get(entry.getId()))
				{
					if (entry.getOption().toLowerCase().contains(exceptOption))
					{
						entries[added] = entry;
						added++;
					}
				}
			}
			MenuEntry[] finalEntries = new MenuEntry[added];
			client.setMenuEntries(Arrays.stream(entries).filter(java.util.Objects::nonNull).collect(Collectors.toList()).toArray(finalEntries));
		}
	}

	@NotNull
	private Boolean hideOption(MenuEntry entry)
	{
		return new ArrayList<>(removeOptions).removeIf(opt -> entry.getOption().toLowerCase().contains(opt.toLowerCase()));
	}

}
