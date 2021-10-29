package net.runelite.client.plugins.adonaicore.menu;

import net.runelite.api.*;

public class Menus
{
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

}
