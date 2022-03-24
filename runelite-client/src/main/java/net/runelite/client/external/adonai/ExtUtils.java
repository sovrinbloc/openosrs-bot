/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.external.adonai;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.queries.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.external.PrayerMap;
import net.runelite.client.external.Spells;
import net.runelite.client.external.Tab;
import net.runelite.client.external.adonaicore.toolbox.Text;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.adonaicore.Adonai;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static java.util.Comparator.naturalOrder;
import static net.runelite.client.external.adonai.ExtUtils.AdonaiDecorativeObjects.findNearestDecorObject;

@Slf4j
@SuppressWarnings("unused")
@Singleton
public class ExtUtils<operator, T2>
{
	private static Client client;
	private Keyboard keyboard = null;

	ExtUtils(Client client, Keyboard keyboard)
	{
		this.client = client;
		this.keyboard = keyboard;
	}

	ExtUtils(Client client)
	{
		this.client = client;
	}

	public int[] stringToIntArray(String string)
	{
		return Arrays.stream(string.split(","))
				.map(String::trim)
				.mapToInt(Integer::parseInt)
				.toArray();
	}

	public static void initialize(Client client)
	{
		AdonaiGameObjects.client = client;
		AdonaiWidgetItems.client = client;
		AdonaiObjects.client = client;
		AdonaiDecorativeObjects.client = client;
		AdonaiEquippedItems.client = client;
		AdonaiWallObjects.client = client;
		Adonai.client = client;
	}

	// find in game items

	public static class AdonaiGameObjects
	{

		public static Client client = Adonai.client;

		@Nullable
		public static GameObject findNearestGameObject(int... ids)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new GameObjectQuery()
					.idEquals(ids)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		@Nullable
		public static GameObject findNearestGameObject(String... names)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new GameObjectQuery()
					.nameEquals(names)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		@Nullable
		public static GameObject findNearestGameObject(Predicate<GameObject> filter)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new GameObjectQuery()
					.filter(filter)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		public static List<GameObject> getGameObjects(int... ids)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new GameObjectQuery()
					.idEquals(ids)
					.result(client)
					.list;
		}

		public static List<GameObject> getGameObjects(String... names)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new GameObjectQuery()
					.nameEquals(names)
					.result(client)
					.list;
		}

		public static List<GameObject> getGameObjects(Predicate<GameObject> filter)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new GameObjectQuery()
					.filter(filter)
					.result(client)
					.list;
		}
	}

	public static class AdonaiWallObjects
	{

		public static Client client = Adonai.client;

		@Nullable
		public static WallObject findNearestWallObject(int... ids)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new WallObjectQuery()
					.idEquals(ids)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		@Nullable
		public static WallObject findNearestWallObject(String... names)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new WallObjectQuery()
					.nameEquals(names)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		@Nullable
		public static WallObject findNearestWallObject(Predicate<WallObject> filter)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new WallObjectQuery()
					.filter(filter)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		public static List<WallObject> getWallObjects(int... ids)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new WallObjectQuery()
					.idEquals(ids)
					.result(client)
					.list;
		}

		public static List<WallObject> getWallObjects(String... names)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new WallObjectQuery()
					.nameEquals(names)
					.result(client)
					.list;
		}

		public static List<WallObject> getWallObjects(Predicate<WallObject> filter)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new WallObjectQuery()
					.filter(filter)
					.result(client)
					.list;
		}
	}

	public static class AdonaiDecorativeObjects
	{

		public static Client client = Adonai.client;

		@Nullable
		public static DecorativeObject findNearestDecorObject(int... ids)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new DecorativeObjectQuery()
					.idEquals(ids)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		@Nullable
		public static DecorativeObject findNearestDecorObject(String... names)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new DecorativeObjectQuery()
					.nameEquals(names)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		@Nullable
		public static DecorativeObject findNearestDecorObject(Predicate<DecorativeObject> filter)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new DecorativeObjectQuery()
					.filter(filter)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		public static List<DecorativeObject> getDecorObjects(int... ids)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new DecorativeObjectQuery()
					.idEquals(ids)
					.result(client)
					.list;
		}

		public static List<DecorativeObject> getDecorObjects(String... names)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new DecorativeObjectQuery()
					.nameEquals(names)
					.result(client)
					.list;
		}

		public static List<DecorativeObject> getDecorObjects(Predicate<DecorativeObject> filter)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new DecorativeObjectQuery()
					.filter(filter)
					.result(client)
					.list;
		}

	}

	public static class AdonaiGroundObjects
	{
		public static Client client = Adonai.client;

		@Nullable
		public static GroundObject findNearestGroundObject(int... ids)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new GroundObjectQuery()
					.idEquals(ids)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		@Nullable
		public static GroundObject findNearestGroundObject(String... names)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new GroundObjectQuery()
					.nameEquals(names)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		@Nullable
		public static GroundObject findNearestGroundObject(Predicate<GroundObject> filter)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new GroundObjectQuery()
					.filter(filter)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		public static List<GroundObject> getGroundObjects(int... ids)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new GroundObjectQuery()
					.idEquals(ids)
					.result(client)
					.list;
		}

		public static List<GroundObject> getGroundObjects(String... names)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new GroundObjectQuery()
					.nameEquals(names)
					.result(client)
					.list;
		}

		public static List<GroundObject> getGroundObjects(Predicate<GroundObject> filter)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return new ArrayList<>();
			}

			return new GroundObjectQuery()
					.filter(filter)
					.result(client)
					.list;
		}

	}

	public static class NPCs
	{
		public static Client client = Adonai.client;

		@Nullable
		public NPC findNearestNPC(int... ids)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new NPCQuery()
					.idEquals(ids)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		@Nullable
		public NPC findNearestNPC(String... names)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new NPCQuery()
					.nameEquals(names)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}

		@Nullable
		public NPC findNearestNPC(Predicate<NPC> filter)
		{
			assert client.isClientThread();

			if (client.getLocalPlayer() == null)
			{
				return null;
			}

			return new NPCQuery()
					.filter(filter)
					.result(client)
					.nearestTo(client.getLocalPlayer());
		}
	}


	public static class AdonaiObjects
	{

		public static Client client = Adonai.client;

		@Nullable
		public TileObject findNearestObject(int... ids)
		{
			GameObject gameObject = AdonaiGameObjects.findNearestGameObject(ids);

			if (gameObject != null)
			{
				return gameObject;
			}

			WallObject wallObject = AdonaiWallObjects.findNearestWallObject(ids);

			if (wallObject != null)
			{
				return wallObject;
			}
			DecorativeObject decorativeObject = findNearestDecorObject(ids);

			if (decorativeObject != null)
			{
				return decorativeObject;
			}

			return AdonaiGroundObjects.findNearestGroundObject(ids);
		}

		@Nullable
		public TileObject findNearestObject(String... names)
		{
			GameObject gameObject = AdonaiGameObjects.findNearestGameObject(names);

			if (gameObject != null)
			{
				return gameObject;
			}

			WallObject wallObject = AdonaiWallObjects.findNearestWallObject(names);

			if (wallObject != null)
			{
				return wallObject;
			}
			DecorativeObject decorativeObject = findNearestDecorObject(names);

			if (decorativeObject != null)
			{
				return decorativeObject;
			}

			return AdonaiGroundObjects.findNearestGroundObject(names);
		}
	}

	public static class AdonaiWidgetItems
	{

		public static Client client = Adonai.client;

		public static List<WidgetItem> getItems(int... itemIDs)
		{
			assert client.isClientThread();

			return new InventoryWidgetItemQuery()
					.idEquals(itemIDs)
					.result(client)
					.list;
		}

		public static List<WidgetItem> getItems(String... names)
		{
			assert client.isClientThread();

			ArrayList<WidgetItem> items = new InventoryWidgetItemQuery()
					.result(client)
					.list;

			new ArrayList<>(Arrays.asList(names)).forEach(inventoryItem ->
					items.removeIf(item -> item.getWidget()
							.getName()
							.equals(inventoryItem)));

			return items;
		}

		public static List<WidgetItem> getItems(Predicate<WidgetItem> filter)
		{
			assert client.isClientThread();

			return new InventoryWidgetItemQuery()
					.filter(filter)
					.result(client)
					.list;
		}

	}

	// widget items
	public static class AdonaiEquippedItems
	{
		public static Client client = Adonai.client;

		public static List<Widget> getEquippedItems(int[] itemIds)
		{
			assert client.isClientThread();

			Widget equipmentWidget = client.getWidget(WidgetInfo.EQUIPMENT);

			List<Integer> equippedIds = new ArrayList<>();

			for (int i : itemIds)
			{
				equippedIds.add(i);
			}

			List<Widget> equipped = new ArrayList<>();

			if (equipmentWidget.getStaticChildren() != null)
			{
				for (Widget widgets : equipmentWidget.getStaticChildren())
				{
					for (Widget items : widgets.getDynamicChildren())
					{
						if (equippedIds.contains(items.getItemId()))
						{
							equipped.add(items);
						}
					}
				}
			}
			else
			{
				log.error("Children is Null!");
			}

			return equipped;
		}

		public static List<Widget> getEquippedItems(String[] names)
		{
			assert client.isClientThread();

			Widget equipmentWidget = client.getWidget(WidgetInfo.EQUIPMENT);

			List<String> equippedNames = new ArrayList<>(Arrays.asList(names));

			List<Widget> equipped = new ArrayList<>();

			assert equipmentWidget != null;
			if (equipmentWidget.getStaticChildren() != null)
			{
				for (Widget widgets : equipmentWidget.getStaticChildren())
				{
					for (Widget items : widgets.getDynamicChildren())
					{
						if (equippedNames.contains(items.getName()))
						{
							equipped.add(items);
						}
					}
				}
			}
			else
			{
				log.error("Children is Null!");
			}

			return equipped;
		}

		public static List<Widget> getEquippedItems()
		{
			assert client.isClientThread();

			Widget equipmentWidget = client.getWidget(WidgetInfo.EQUIPMENT);

			List<Widget> equipped = new ArrayList<>();

			assert equipmentWidget != null;
			if (equipmentWidget.getStaticChildren() != null)
			{
				for (Widget widgets : equipmentWidget.getStaticChildren())
				{
					equipped.addAll(Arrays.asList(widgets.getDynamicChildren()));
				}
			}
			else
			{
				log.error("Children is Null!");
			}

			return equipped;
		}
	}

	public static class AdonaiScreen
	{
		public static Point getScreenPosition(Point point)
		{
			java.awt.Point locationOnScreen = client.getCanvas()
					.getLocationOnScreen();
			return new Point(
					point.getX() + (int) locationOnScreen.getX(),
					point.getY() + (int) locationOnScreen.getY()
			);
		}

		public static Point getClickPoint(@NotNull Rectangle rect)
		{
			final int x = (int) (rect.getX() + getRandomIntBetweenRange(
					(int) rect.getWidth() / 6 * -1,
					(int) rect.getWidth() / 6
			) + rect.getWidth() / 2);
			final int y = (int) (rect.getY() + getRandomIntBetweenRange(
					(int) rect.getHeight() / 6 * -1,
					(int) rect.getHeight() / 6
			) + rect.getHeight() / 2);

			return new Point(x, y);
		}

		public static int getRandomIntBetweenRange(int min, int max)
		{
			return (int) ((Math.random() * ((max - min) + 1)) + min);
		}

		public static boolean isOnScreen(TileObject point)
		{
			return client.getCanvas()
					.contains(
							point.getCanvasLocation()
									.getX(),
							point.getCanvasLocation()
									.getY()
					);
		}

		public static boolean isOnScreen(WidgetItem point)
		{
			return client.getCanvas()
					.contains(
							point.getCanvasLocation()
									.getX(),
							point.getCanvasLocation()
									.getY()
					);
		}

		public static boolean isOnScreen(Actor actor)
		{
			assert client.isClientThread();
			// that was my last resort that worked
			client.getCanvas()
					.getLocationOnScreen();
			client.getCanvas()
					.isShowing();
			client.getCanvas()
					.getMousePosition();
			client.getCanvasHeight();
			client.getCanvasWidth();
			Point actorPoint = Perspective.localToCanvas(client, actor.getLocalLocation(), client.getPlane());
			assert actorPoint != null;
			return isOnScreen(actorPoint);
		}

		public static boolean isOnScreen(Point point)
		{
			assert client.isClientThread();
			return (point.getX() >= 0 && point.getX() <= client.getCanvasWidth() && point.getY() >= 0 &&
					point.getY() <= client.getCanvasHeight());
		}

	}

	public int getTabHotkey(Tab tab)
	{
		assert client.isClientThread();

		final int var    = client.getVarbitValue(client.getVarps(), tab.getVarbit());
		final int offset = 111;

		switch (var)
		{
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
				return var + offset;
			case 13:
				return 27;
			default:
				return -1;
		}
	}

	public WidgetInfo getSpellWidgetInfo(String spell)
	{
		assert client.isClientThread();
		return Spells.getWidget(spell);
	}

	public WidgetInfo getPrayerWidgetInfo(String spell)
	{
		assert client.isClientThread();
		return PrayerMap.getWidget(spell);
	}

	public Widget getSpellWidget(String spell)
	{
		assert client.isClientThread();
		return client.getWidget(Spells.getWidget(spell));
	}

	public Widget getPrayerWidget(String spell)
	{
		assert client.isClientThread();
		return client.getWidget(PrayerMap.getWidget(spell));
	}

	/**
	 * This method must be called on a new
	 * thread, if you try to call it on
	 * {@link net.runelite.client.callback.ClientThread}
	 * it will result in a crash/desynced thread.
	 */
	public void typeString(String string)
	{
		assert !client.isClientThread();

		try
		{
			keyboard.type(string);
		}
		catch (InterruptedException e)
		{
			log.debug(e.toString());
			e.printStackTrace();
		}
	}

	private void keyEvent(int id, char key)
	{
		assert !client.isClientThread();

		KeyEvent e = new KeyEvent(
				client.getCanvas(), id, System.currentTimeMillis(),
				0, KeyEvent.VK_UNDEFINED, key
		);

		client.getCanvas()
				.dispatchEvent(e);
	}

	/**
	 * This method must be called on a new
	 * thread, if you try to call it on
	 * {@link net.runelite.client.callback.ClientThread}
	 * it will result in a crash/desynced thread.
	 */
	public void click(Rectangle rectangle)
	{
		assert !client.isClientThread();
		Point point = getClickPoint(rectangle);
		click(point);
	}

	public void click(Point p)
	{
		assert !client.isClientThread();

		if (client.isStretchedEnabled())
		{
			final Dimension stretched      = Adonai.client.getStretchedDimensions();
			final Dimension realDimensions = Adonai.client.getRealDimensions();
			final double    width          = (stretched.width / realDimensions.getWidth());
			final double    height         = (stretched.height / realDimensions.getHeight());
			final Point     point          = new Point((int) (p.getX() * width), (int) (p.getY() * height));
			mouseEvent(501, point);
			mouseEvent(502, point);
			mouseEvent(500, point);
			return;
		}
		mouseEvent(501, p);
		mouseEvent(502, p);
		mouseEvent(500, p);
	}

	public Point getClickPoint(@NotNull Rectangle rect)
	{
		final int x = (int) (rect.getX() + getRandomIntBetweenRange(
				(int) rect.getWidth() / 6 * -1,
				(int) rect.getWidth() / 6
		) + rect.getWidth() / 2);
		final int y = (int) (rect.getY() + getRandomIntBetweenRange(
				(int) rect.getHeight() / 6 * -1,
				(int) rect.getHeight() / 6
		) + rect.getHeight() / 2);

		return new Point(x, y);
	}

	public int getRandomIntBetweenRange(int min, int max)
	{
		return (int) ((Math.random() * ((max - min) + 1)) + min);
	}

	private void mouseEvent(int id, @NotNull Point point)
	{
		MouseEvent e = new MouseEvent(
				client.getCanvas(), id,
				System.currentTimeMillis(),
				0, point.getX(), point.getY(),
				1, false, 1
		);

		client.getCanvas()
				.dispatchEvent(e);
	}

	public boolean isOnScreen(TileObject point)
	{
		return client.getCanvas()
				.contains(
						point.getCanvasLocation()
								.getX(),
						point.getCanvasLocation()
								.getY()
				);
	}

	public boolean isOnScreen(WidgetItem point)
	{
		return client.getCanvas()
				.contains(
						point.getCanvasLocation()
								.getX(),
						point.getCanvasLocation()
								.getY()
				);
	}

	public boolean isOnScreen(Actor actor)
	{
		assert client.isClientThread();
		// that was my last resort that worked
		client.getCanvas()
				.getLocationOnScreen();
		client.getCanvas()
				.isShowing();
		client.getCanvas()
				.getMousePosition();
		client.getCanvasHeight();
		client.getCanvasWidth();
		Point actorPoint = Perspective.localToCanvas(client, actor.getLocalLocation(), client.getPlane());
		assert actorPoint != null;
		return isOnScreen(actorPoint);
	}

	public boolean isOnScreen(Point point)
	{
		assert client.isClientThread();
		return (point.getX() >= 0 && point.getX() <= client.getCanvasWidth() && point.getY() >= 0 &&
				point.getY() <= client.getCanvasHeight());
	}

	private void robotClick()
			throws AWTException
	{
		Robot bot = new Robot();
		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

	}

	public void robotType(String something)
	{
		try
		{
			this.keyboard.type(something);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static int random(int min, int max)
	{
		return ThreadLocalRandom.current()
				.nextInt(min, max);
	}

	public static class Inventory
	{
		private static Client client = Adonai.client;
		private static ItemManager itemManager;

		public static enum InventoryDropMode
		{
			LEFT_RIGHT_TOP_DOWN,
			TOP_DOWN_LEFT_RIGHT,
			LEFT_RIGHT_BOTTOM_UP,
		}

		// left to right, top to bottom
		final public static Comparator<Rectangle> DROP_SORT_NORMAL_HORIZONTAL = Comparator.comparingDouble(
						// Negate to have the furthest first
						Rectangle::getY)
				// Order by position
				.thenComparingDouble(Rectangle::getX);

		// left to right, bottom to top
		final public static Comparator<Rectangle> DROP_SORT_HORIZONTAL_BOTTOM_UP = Comparator.comparingDouble(
						// Negate to have the furthest first
						Rectangle::getY)
				.reversed()
				// Order by position
				.thenComparingDouble(Rectangle::getX);

		// top to bottom, left to right
		final public static Comparator<Rectangle> DROP_SORT_VERTICAL = Comparator.comparingDouble(
						// Negate to have the furthest first
						Rectangle::getX)
				// Order by position
				.thenComparingDouble(Rectangle::getY);

		public static Comparator<Rectangle> getDropMode(InventoryDropMode mode)
		{
			switch (mode)
			{
				case LEFT_RIGHT_BOTTOM_UP:
					return DROP_SORT_HORIZONTAL_BOTTOM_UP;
				case TOP_DOWN_LEFT_RIGHT:
					return DROP_SORT_VERTICAL;
				default:
					return DROP_SORT_NORMAL_HORIZONTAL;
			}
		}

		public static Point getInventoryLocation()
		{
			return null;
		}

		public static void init(ItemManager itemManager)
		{
			Inventory.itemManager = itemManager;
		}

		public static InventoryItem[] convertToInventoryItems(final Item[] items)
		{
			final InventoryItem[] out = new InventoryItem[items.length];
			for (int i = 0; i < items.length; i++)
			{
				InventoryItem iItem = getInventoryItem(items[i], i);
				out[i] = iItem;
			}

			return out;
		}

		@NotNull
		private static ExtUtils.Inventory.InventoryItem getInventoryItem(Item item, int slot)
		{
			final ItemComposition c     = itemManager.getItemComposition(item.getId());
			InventoryItem         iItem = new InventoryItem(slot, item, c.getName(), c.isStackable());
			iItem.setBounds(invBounds(item.getId()));
			return iItem;
		}

		public static Rectangle invBounds(int id)
		{
			final Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

			if (inventoryWidget == null)
			{
				return null;
			}

			for (WidgetItem item : inventoryWidget.getWidgetItems())
			{
				if (item.getId() == id)
				{
					return item.getCanvasBounds();
				}
			}

			return null;
		}

		public static List<Rectangle> listOfBounds(int id)
		{
			final Widget          inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
			final List<Rectangle> bounds          = new ArrayList<>();

			if (inventoryWidget == null)
			{
				return bounds;
			}

			for (WidgetItem item : inventoryWidget.getWidgetItems())
			{
				if (item.getId() == id)
				{
					bounds.add(item.getCanvasBounds());
				}
			}
			return bounds;
		}

		InventoryItem getItemByName(String name)
		{
			ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);

			final Item[]                       items          = itemContainer.getItems();
			InventoryItem[] inventoryItems = Inventory.convertToInventoryItems(items);
			for (InventoryItem item :
					inventoryItems)
			{
				if (item.getName()
						.equals(name))
				{
					return item;
				}
			}
			return null;
		}

		public static List<InventoryItem> getItemsExceptById(List<Integer> itemIds)
		{
			ItemContainer       itemContainer = client.getItemContainer(InventoryID.INVENTORY);
			List<InventoryItem> iItems        = new ArrayList<>();

			final Item[]                       items          = itemContainer.getItems();
			InventoryItem[] inventoryItems = Inventory.convertToInventoryItems(items);
			for (InventoryItem item :
					inventoryItems)
			{
				if (!itemIds.contains(item.getItem()
						.getId()))
				{
					iItems.add(item);
				}
			}
			return iItems;
		}

		public static List<InventoryItem> getItemsExceptByName(List<String> itemNames)
		{
			ItemContainer       itemContainer = client.getItemContainer(InventoryID.INVENTORY);
			List<InventoryItem> iItems        = new ArrayList<>();

			final Item[]                       items          = itemContainer.getItems();
			InventoryItem[] inventoryItems = Inventory.convertToInventoryItems(items);

			List<String> cleanNames = new ArrayList<>();
			itemNames.forEach(n -> {
				String cleanName = Text.removeWhitespace(n.toLowerCase());
				if (!cleanName.isEmpty())
				{
					cleanNames.add(cleanName);
				}
			});

			for (InventoryItem item :
					inventoryItems)
			{
				if (!cleanNames.contains(Text.removeWhitespace(item.getName().toLowerCase())))
				{
					iItems.add(item);
				}
			}
			return iItems;
		}

		@NotNull
		public static List<Rectangle> getNonStackableInventoryRectanglesExceptById(List<Integer> ids)
		{
			List<InventoryItem> items = Inventory.getItemsExceptById(ids);
			log.info("Items size (except necessity items [by id]): {}", items.size());

			return getInventoryItemsBounds(items);
		}

		@NotNull
		public static List<Rectangle> getAllNonStackableInventoryRectangles(List<Integer> ids)
		{
			List<InventoryItem> items = Inventory.getItemsExceptById(ids);
			log.info("Items size (except necessity items [by id]): {}", items.size());

			return getInventoryItemsBounds(items);
		}

		public static int getAllNonStackableInventoryCount()
		{
			List<Integer>       ids   = new ArrayList<>();
			List<InventoryItem> items = Inventory.getItemsExceptById(ids);

			return getInventoryItemsBounds(items).size();
		}

		@NotNull
		public static List<Rectangle> getNonStackableInventoryRectanglesExceptByName(List<String> names)
		{
			List<InventoryItem> items = Inventory.getItemsExceptByName(names);
			log.info("Items size (except necessity items [by name]): {}", items.size());

			return getInventoryItemsBounds(items);
		}

		@NotNull
		private static List<Rectangle> getInventoryItemsBounds(List<InventoryItem> itemsExcept)
		{
			// list of unique item ids
			List<Integer> objects = new ArrayList<>();

			for (InventoryItem item :
					itemsExcept)
			{
				if (objects.contains(item.getItem().getId())) {
					continue;
				}
				objects.add(item.getItem().getId());
			}

			log.info("Length of items: {}", objects.size());

			// this will store all the rectangles of the items in the inventory (even duplicate rects)
			List<Rectangle> invBounds = new ArrayList<>();

			// get the integer id of each, and then get each rectangle of the items in the inventory, and
			// add them to a list.
			for (int id : objects)
			{
				invBounds.addAll(Inventory.listOfBounds(id));
			}

			Comparator<Rectangle> COMPARATOR = Comparator
					.comparing(Rectangle::getY, Comparator.nullsLast(naturalOrder()))
					.thenComparing(Rectangle::getX);

			log.info("List of inventory bounds is {} long", invBounds.size());
			return invBounds;
		}

		public static List<InventoryItem> getItems(List<Integer> itemIds)
		{
			ItemContainer       itemContainer = client.getItemContainer(InventoryID.INVENTORY);
			List<InventoryItem> iItems        = new ArrayList<>();

			final Item[]                       items          = itemContainer.getItems();
			InventoryItem[] inventoryItems = Inventory.convertToInventoryItems(items);
			for (InventoryItem item :
					inventoryItems)
			{
				if (itemIds.contains(item.getItem()
						.getId()))
				{
					iItems.add(item);
				}
			}
			return iItems;
		}

		public static InventoryItem getItem(int itemId)
		{
			ItemContainer       itemContainer = client.getItemContainer(InventoryID.INVENTORY);
			List<InventoryItem> iItems        = new ArrayList<>();

			final Item[]                       items          = itemContainer.getItems();
			InventoryItem[] inventoryItems = Inventory.convertToInventoryItems(items);
			for (InventoryItem item :
					inventoryItems)
			{
				if (itemId == item.getItem()
						.getId())
				{
					return item;
				}
			}
			return null;
		}

		public static int getInventorySize()
		{
			ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);

			final Item[] items    = itemContainer.getItems();
			int          itemSize = 0;
			for (int i = 0; i < 28; i++)
			{
				if (i < items.length)
				{
					final Item item = items[i];
					if (item.getQuantity() > 0)
					{
						if (!Inventory.getItem(item.getId())
								.isStackable())
						{
							itemSize += item.getQuantity();
							continue;
						}
						itemSize++;
					}
				}
			}

			return itemSize;
		}


		@Data
		public static class InventoryItem implements Clickable
		{
			private final int slot;
			private Item item;
			private final String name;
			private final boolean stackable;

			InventoryItem(int slot, Item item, String name, boolean stackable)
			{
				this.slot = slot;
				this.item = item;
				this.name = name;
				this.stackable = stackable;
			}

			@Nullable
			private Rectangle bounds;

			@Override
			public Rectangle getBounds()
			{
				return bounds;
			}

			@Override
			public void setBounds(Rectangle bounds)
			{
				this.bounds = bounds;
			}
		}

	}
}
