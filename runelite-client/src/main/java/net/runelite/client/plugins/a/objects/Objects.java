package net.runelite.client.plugins.a.objects;

import net.runelite.api.*;
import net.runelite.api.queries.DecorativeObjectQuery;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.queries.GroundObjectQuery;
import net.runelite.api.queries.WallObjectQuery;
import net.runelite.client.plugins.a.Adonai;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public  class Objects
{

	public static List<GameObject> getGameObjects(int... ids)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new GameObjectQuery()
				.idEquals(ids)
				.result(Adonai.client)
				.list;
	}

	public static List<GameObject> getGameObjects(String... names)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new GameObjectQuery()
				.nameEquals(names)
				.result(Adonai.client)
				.list;
	}

	public static List<GameObject> getGameObjects(Predicate<GameObject> filter)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new GameObjectQuery()
				.filter(filter)
				.result(Adonai.client)
				.list;
	}

	public static List<WallObject> getWallObjects(int... ids)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new WallObjectQuery()
				.idEquals(ids)
				.result(Adonai.client)
				.list;
	}

	public static List<WallObject> getWallObjects(String... names)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new WallObjectQuery()
				.nameEquals(names)
				.result(Adonai.client)
				.list;
	}

	public static List<WallObject> getWallObjects(Predicate<WallObject> filter)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new WallObjectQuery()
				.filter(filter)
				.result(Adonai.client)
				.list;
	}

	public static List<DecorativeObject> getDecorObjects(int... ids)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new DecorativeObjectQuery()
				.idEquals(ids)
				.result(Adonai.client)
				.list;
	}

	public static List<DecorativeObject> getDecorObjects(String... names)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new DecorativeObjectQuery()
				.nameEquals(names)
				.result(Adonai.client)
				.list;
	}

	public static List<DecorativeObject> getDecorObjects(Predicate<DecorativeObject> filter)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new DecorativeObjectQuery()
				.filter(filter)
				.result(Adonai.client)
				.list;
	}

	public static List<GroundObject> getGroundObjects(int... ids)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new GroundObjectQuery()
				.idEquals(ids)
				.result(Adonai.client)
				.list;
	}

	public static List<GroundObject> getGroundObjects(String... names)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new GroundObjectQuery()
				.nameEquals(names)
				.result(Adonai.client)
				.list;
	}

	public static List<GroundObject> getGroundObjects(Predicate<GroundObject> filter)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return new ArrayList<>();
		}

		return new GroundObjectQuery()
				.filter(filter)
				.result(Adonai.client)
				.list;
	}

	@Nullable
	public static TileObject findNearestObject(int... ids)
	{
		GameObject gameObject = findNearestGameObject(ids);

		if (gameObject != null)
		{
			return gameObject;
		}

		WallObject wallObject = findNearestWallObject(ids);

		if (wallObject != null)
		{
			return wallObject;
		}
		DecorativeObject decorativeObject = findNearestDecorObject(ids);

		if (decorativeObject != null)
		{
			return decorativeObject;
		}

		return findNearestGroundObject(ids);
	}

	@Nullable
	public static TileObject findNearestObject(String... names)
	{
		GameObject gameObject = findNearestGameObject(names);

		if (gameObject != null)
		{
			return gameObject;
		}

		WallObject wallObject = findNearestWallObject(names);

		if (wallObject != null)
		{
			return wallObject;
		}
		DecorativeObject decorativeObject = findNearestDecorObject(names);

		if (decorativeObject != null)
		{
			return decorativeObject;
		}

		return findNearestGroundObject(names);
	}

	@Nullable
	public static TileObject findNearestObject(String name)
	{
		GameObject gameObject = findNearestGameObject(obj -> obj.getName().contains(name));

		if (gameObject != null)
		{
			return gameObject;
		}

		WallObject wallObject = findNearestWallObject(obj -> obj.getName().contains(name));

		if (wallObject != null)
		{
			return wallObject;
		}
		DecorativeObject decorativeObject = findNearestDecorObject(obj -> obj.getName().contains(name));

		if (decorativeObject != null)
		{
			return decorativeObject;
		}

		return findNearestGroundObject(obj -> obj.getName().contains(name));
	}


	// find in game items

	@Nullable
	public static GameObject findNearestGameObject(int... ids)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new GameObjectQuery()
				.idEquals(ids)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static GameObject findNearestGameObject(String... names)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new GameObjectQuery()
				.nameEquals(names)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static GameObject findNearestGameObject(Predicate<GameObject> filter)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new GameObjectQuery()
				.filter(filter)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static WallObject findNearestWallObject(int... ids)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new WallObjectQuery()
				.idEquals(ids)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static WallObject findNearestWallObject(String... names)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new WallObjectQuery()
				.nameEquals(names)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static WallObject findNearestWallObject(Predicate<WallObject> filter)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new WallObjectQuery()
				.filter(filter)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static DecorativeObject findNearestDecorObject(int... ids)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new DecorativeObjectQuery()
				.idEquals(ids)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static DecorativeObject findNearestDecorObject(String... names)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new DecorativeObjectQuery()
				.nameEquals(names)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static DecorativeObject findNearestDecorObject(Predicate<DecorativeObject> filter)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new DecorativeObjectQuery()
				.filter(filter)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static GroundObject findNearestGroundObject(int... ids)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new GroundObjectQuery()
				.idEquals(ids)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static GroundObject findNearestGroundObject(String... names)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new GroundObjectQuery()
				.nameEquals(names)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public static GroundObject findNearestGroundObject(Predicate<GroundObject> filter)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new GroundObjectQuery()
				.filter(filter)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}



	public static TileObject findTileObject(int x, int y, int id)
	{
		Scene scene = Adonai.client.getScene();
		Tile[][][] tiles = scene.getTiles();
		Tile tile = tiles[Adonai.client.getPlane()][x][y];
		if (tile != null)
		{
			for (GameObject gameObject : tile.getGameObjects())
			{
				if (gameObject != null && gameObject.getId() == id)
				{
					return gameObject;
				}
			}

			WallObject wallObject = tile.getWallObject();
			if (wallObject != null && wallObject.getId() == id)
			{
				return wallObject;
			}

			DecorativeObject decorativeObject = tile.getDecorativeObject();
			if (decorativeObject != null && decorativeObject.getId() == id)
			{
				return decorativeObject;
			}

			GroundObject groundObject = tile.getGroundObject();
			if (groundObject != null && groundObject.getId() == id)
			{
				return groundObject;
			}
		}
		return null;
	}
}
