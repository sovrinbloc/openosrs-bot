package net.runelite.client.external.adonaicore.menu;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;

import java.util.List;

@Slf4j
public class Menus
{
	public enum TileType
	{
		WALL_OBJECT_TYPE,
		DECORATIVE_OBJECT_TYPE,
		GROUND_OBJECT_TYPE,
		GAME_OBJECT_TYPE,
		TILE_ITEM_TYPE
	}

	public static boolean objectIdEquals(Client client, TileObject tileObject, int id)
	{
		if (tileObject == null)
		{
			return false;
		}

		if (tileObject.getId() == id)
		{
			return true;
		}

		// Menu action EXAMINE_OBJECT sends the transformed object id, not the base id, unlike
		// all of the GAME_OBJECT_OPTION actions, so check the id against the impostor ids
		final ObjectComposition comp = client.getObjectDefinition(tileObject.getId());

		if (comp.getImpostorIds() != null)
		{
			for (int impostorId : comp.getImpostorIds())
			{
				if (impostorId == id)
				{
					return true;
				}
			}
		}

		return false;
	}

	public static boolean groundItemIdEquals(Client client, TileItem tileObject, int id)
	{
		if (tileObject == null)
		{
			return false;
		}

		if (tileObject.getId() == id)
		{
			return true;
		}

		// Menu action EXAMINE_OBJECT sends the transformed object id, not the base id, unlike
		// all of the GAME_OBJECT_OPTION actions, so check the id against the impostor ids
		final ObjectComposition comp = client.getObjectDefinition(tileObject.getId());

		if (comp.getImpostorIds() != null)
		{
			for (int impostorId : comp.getImpostorIds())
			{
				if (impostorId == id)
				{
					return true;
				}
			}
		}

		return false;
	}

	public static TileObject findTileObject(Client client, Tile tile, int id)
	{
		if (tile == null)
		{
			return null;
		}

		final GameObject[] tileGameObjects = tile.getGameObjects();
		final DecorativeObject tileDecorativeObject = tile.getDecorativeObject();
		final WallObject tileWallObject = tile.getWallObject();
		final GroundObject groundObject = tile.getGroundObject();

		if (objectIdEquals(client, tileWallObject, id))
		{
			return tileWallObject;
		}

		if (objectIdEquals(client, tileDecorativeObject, id))
		{
			return tileDecorativeObject;
		}

		if (objectIdEquals(client, groundObject, id))
		{
			return groundObject;
		}

		for (GameObject object : tileGameObjects)
		{
			if (objectIdEquals(client, object, id))
			{
				return object;
			}
		}

		return null;
	}

	public static TileItem findTileItem(Client client, Tile tile, int id)
	{
		if (tile == null)
		{
			return null;
		}

		final List<TileItem> tileGroundItems = tile.getGroundItems();

		for (TileItem object : tileGroundItems)
		{
			if (groundItemIdEquals(client, object, id))
			{
				return object;
			}
		}

		return null;
	}

	private static int tick = 0;
	/**
	 * Get the type of the item
	 * @param client the game client
	 * @param tile the tile which contains the alleged object
	 * @param id the id of the menu
	 */
	public static TileType getTileObjectType(Client client, Tile tile, int id)
	{
		tick++;
		if (tile == null)
		{
			return null;
		}

		final GameObject[] tileGameObjects = tile.getGameObjects();
		final DecorativeObject tileDecorativeObject = tile.getDecorativeObject();
		final WallObject tileWallObject = tile.getWallObject();
		final GroundObject groundObject = tile.getGroundObject();
		List<TileItem> tileGroundItems = null;
		try {
			tileGroundItems = tile.getGroundItems();
		} catch(Exception e)
		{
			if (tick % 100 == 0)
			{
				log.info("There was an exception trying to get the tileGroundItems: {}", e.toString());
			}
		}

		if (objectIdEquals(client, tileWallObject, id))
		{
			return TileType.WALL_OBJECT_TYPE;
		}

		if (objectIdEquals(client, tileDecorativeObject, id))
		{
			return TileType.DECORATIVE_OBJECT_TYPE;
		}

		if (objectIdEquals(client, groundObject, id))
		{
			return TileType.GROUND_OBJECT_TYPE;
		}

		for (GameObject object : tileGameObjects)
		{
			if (objectIdEquals(client, object, id))
			{
				return TileType.GAME_OBJECT_TYPE;
			}
		}

		if (tileGroundItems != null)
		{
			if (tileGroundItems.isEmpty())
			{
				log.info("Huoston, there is a problem, the tileGroundItems is empty!");
			} else {
				for (TileItem item : tileGroundItems)
				{
					if (groundItemIdEquals(client, item, id))
					{
						return TileType.TILE_ITEM_TYPE;
					}
				}
			}
		}

		return null;
	}

}
