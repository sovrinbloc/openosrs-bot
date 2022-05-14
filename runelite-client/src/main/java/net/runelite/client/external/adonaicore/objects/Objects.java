package net.runelite.client.external.adonaicore.objects;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.queries.DecorativeObjectQuery;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.queries.GroundObjectQuery;
import net.runelite.api.queries.WallObjectQuery;
import net.runelite.api.widgets.menu.MenuTargetIdentifier;
import net.runelite.client.plugins.adonaicore.Adonai;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class Objects
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


	public static TileObject getTileObject(MenuTargetIdentifier tileObject)
	{
		return getTileObject(tileObject.getX(), tileObject.getY(), tileObject.getId());
	}

	public static List<TileItem> getGroundItem(int sceneX, int sceneY)
	{
		assert Adonai.client.isClientThread();

		final WorldPoint worldPoint = WorldPoint.fromScene(Adonai.client, sceneX, sceneY, Adonai.client.getPlane());
		Scene scene = Adonai.client.getScene();
		Tile[][][] tiles = scene.getTiles();
		Tile tile = tiles[Adonai.client.getPlane()][sceneX][sceneY];
		return tile.getGroundItems();
	}

	public static List<TileItem> getAllGroundItems()
	{
		assert Adonai.client.isClientThread();

		List<TileItem> items = new ArrayList<>();

		Scene scene = Adonai.client.getScene();
		Tile[][][] tiles = scene.getTiles();
		int z = Adonai.client.getPlane();
		for (int x = 0; x < Constants.SCENE_SIZE; ++x)
		{
			for (int y = 0; y < Constants.SCENE_SIZE; ++y)
			{
				Tile tile = tiles[z][x][y];
				if (tile == null)
				{
					continue;
				}
				List<TileItem> groundItems = tile.getGroundItems();
				if (groundItems.size() > 0)
				{
					items.addAll(groundItems);
				}
			}
		}

		return items;
	}

	public static TileItem findNearestGroundItems()
	{
		assert Adonai.client.isClientThread();

		LocalPoint localLocation = Adonai.client.getLocalPlayer().getLocalLocation();

		List<TileItem> allGroundItems = getAllGroundItems();
		return allGroundItems.stream()
				.min(
						Comparator.comparing(
								entityType -> entityType.getTile().getLocalLocation().distanceTo(
										Adonai.client.getLocalPlayer().getLocalLocation())))
				.orElse(null);
	}

	public static TileItem findNearestGroundItems(int... ids)
	{
		assert Adonai.client.isClientThread();

		List<TileItem> allGroundItems = getAllGroundItems();
		TileItem tileItem = null;
		int distance = 2147483647;

		for (TileItem item : allGroundItems)
		{
			for(int id : ids)
			{
				if (item.getId() == id)
				{
					int tmpDistance = item.getTile().getLocalLocation().distanceTo(Adonai.client.getLocalPlayer().getLocalLocation());
					if (tileItem == null ||
							tmpDistance < distance
					)
					tileItem = item;
					distance = tmpDistance;
				}
			}
		}

		return tileItem;
	}

	public static List<TileItem> getGroundItem()
	{
		assert Adonai.client.isClientThread();

		Player player = Adonai.client.getLocalPlayer();
		if (player == null)
		{
			return null;
		}

		LocalPoint localPoint = player.getLocalLocation();
		Scene scene = Adonai.client.getScene();

		Tile[][][] tiles = scene.getTiles();
		Tile tile = tiles[Adonai.client.getPlane()][localPoint.getSceneX()][localPoint.getSceneY()];
		return tile.getGroundItems();
	}

	public static List<TileItem> getGroundItem(MenuEntry entry)
	{
		assert Adonai.client.isClientThread();

		return getGroundItem(entry.getActionParam0(), entry.getParam1());
	}

	public static TileObject getTileObject(int x, int y, int id)
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

	public static NPC findNpc(int id)
	{
		return Adonai.client.getCachedNPCs()[id];
	}

	public static Player findPlayer(int id)
	{
		return Adonai.client.getCachedPlayers()[id];
	}
}
