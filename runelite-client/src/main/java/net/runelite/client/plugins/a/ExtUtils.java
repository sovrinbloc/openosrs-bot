/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.a;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.queries.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.external.PrayerMap;
import net.runelite.client.external.Spells;
import net.runelite.client.external.Tab;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@SuppressWarnings("unused")
@Singleton
public class ExtUtils
{
	final private Client client;
	private Keyboard keyboard;
	private ChatMessageManager chatMessageManager;

	public ExtUtils(Client client)
	{
		initializeAdonaiClient(client);
		this.client = client;
	}

	ExtUtils(Client client, Keyboard keyboard)
	{
		initializeAdonaiClient(client);
		this.client = client;
		this.keyboard = keyboard;
	}

	public ExtUtils(Client client, ChatMessageManager manager)
	{
		initializeAdonaiClient(client);
		initializeAdonaiChatManager(manager);
		this.client = client;
		this.chatMessageManager = manager;
	}

	public void initializeAdonaiClient(Client client)
	{
		if (!Adonai.isClientInitialized())
		{
			Adonai.initializeClient(client);
		}
	}

	public void initializeAdonaiChatManager(ChatMessageManager manager)
	{
		if (!Adonai.isChatManagerInitialized())
		{
			Adonai.initializeChatMessageManager(manager);
		}
	}


	public void setChatMessageManager(ChatMessageManager manager)
	{
		this.chatMessageManager = manager;
	}

	public void setKeyboardManager(Keyboard keyboard)
	{
		this.keyboard = keyboard;
	}

	public static int[] stringToIntArray(String string)
	{
		return Arrays.stream(string.split(","))
				.map(String::trim)
				.mapToInt(Integer::parseInt)
				.toArray();
	}


	/*
		Bank Items
	 */

	public int getTabHotkey(Tab tab)
	{
		assert client.isClientThread();

		final int var = client.getVarbitValue(client.getVarps(), tab.getVarbit());
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
			final Dimension stretched = client.getStretchedDimensions();
			final Dimension real = client.getRealDimensions();
			final double width = (stretched.width / real.getWidth());
			final double height = (stretched.height / real.getHeight());
			final Point point = new Point((int) (p.getX() * width), (int) (p.getY() * height));
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


	public Point mapWorldPointToGraphicsPoint(WorldPoint worldPoint)
	{
		RenderOverview ro = client.getRenderOverview();

		if (!ro.getWorldMapData()
				.surfaceContainsPosition(worldPoint.getX(), worldPoint.getY()))
		{
			return null;
		}

		float pixelsPerTile = ro.getWorldMapZoom();

		Widget map = client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
		if (map != null)
		{
			Rectangle worldMapRect = map.getBounds();

			int widthInTiles = (int) Math.ceil(worldMapRect.getWidth() / pixelsPerTile);
			int heightInTiles = (int) Math.ceil(worldMapRect.getHeight() / pixelsPerTile);

			Point worldMapPosition = ro.getWorldMapPosition();

			//Offset in tiles from anchor sides
			int yTileMax = worldMapPosition.getY() - heightInTiles / 2;
			int yTileOffset = (yTileMax - worldPoint.getY() - 1) * -1;
			int xTileOffset = worldPoint.getX() + widthInTiles / 2 - worldMapPosition.getX();

			int xGraphDiff = ((int) (xTileOffset * pixelsPerTile));
			int yGraphDiff = (int) (yTileOffset * pixelsPerTile);

			//Center on tile.
			yGraphDiff -= pixelsPerTile - Math.ceil(pixelsPerTile / 2);
			xGraphDiff += pixelsPerTile - Math.ceil(pixelsPerTile / 2);

			yGraphDiff = worldMapRect.height - yGraphDiff;
			yGraphDiff += (int) worldMapRect.getY();
			xGraphDiff += (int) worldMapRect.getX();

			return new Point(xGraphDiff, yGraphDiff);
		}
		return null;
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

	public boolean isTabOpen(WidgetInfo info)
	{
		return Objects.requireNonNull(client.getWidget(info))
				.isHidden();
	}

	enum ToolbarTabs
	{
		COMBAT_OPTIONS,
		SKILLS,
		QUEST_LIST,
		INVENTORY,
		WORN_EQUIPMENT,
		PRAYER,
		MAGIC,
		FRIENDS_LIST,
		ACCOUNT_MANAGEMENT,
		CHAT_CHANNEL,
		SETTINGS,
		EMOTES,
		MUSIC_PLAYER
	}

	HashMap<ToolbarTabs, int[]> something = new HashMap<>()
	{{
		put(ToolbarTabs.COMBAT_OPTIONS, new int[]{164, 66});
		put(ToolbarTabs.SKILLS, new int[]{164, 67});
		put(ToolbarTabs.QUEST_LIST, new int[]{164, 68});
		put(ToolbarTabs.INVENTORY, new int[]{164, 69});
		put(ToolbarTabs.WORN_EQUIPMENT, new int[]{164, 70});
		put(ToolbarTabs.PRAYER, new int[]{164, 71});
		put(ToolbarTabs.MAGIC, new int[]{164, 72});

		put(ToolbarTabs.FRIENDS_LIST, new int[]{164, 52});
		put(ToolbarTabs.ACCOUNT_MANAGEMENT, new int[]{164, 51});
		put(ToolbarTabs.CHAT_CHANNEL, new int[]{164, 50});
		put(ToolbarTabs.SETTINGS, new int[]{164, 53});
		put(ToolbarTabs.EMOTES, new int[]{164, 54});
		put(ToolbarTabs.MUSIC_PLAYER, new int[]{164, 55});
	}};

	enum MapTabs
	{
		ACTIVATE_QUICK_PRAYERS,
		TOGGLE_RUN,
		SPECIAL_ATTACK,
		WORLD_MAP
	}

	HashMap<MapTabs, int[]> mapTabs = new HashMap<>()
	{{
		put(MapTabs.ACTIVATE_QUICK_PRAYERS, new int[]{160, 17});
		put(MapTabs.SPECIAL_ATTACK, new int[]{160, 33});
		put(MapTabs.WORLD_MAP, new int[]{160, 48});
		put(MapTabs.TOGGLE_RUN, new int[]{160, 25});
	}};

	boolean inventoryContains(String... names)
	{
		if (isTabOpen(WidgetInfo.INVENTORY))
		{
			Widget widget = client.getWidget(15, 3);
			if (widget != null)
			{
				Widget[] children = widget.getChildren();
				assert children != null;
				for (String name : names)
				{
					for (Widget child : children)
					{
						if (child.getName()
								.contains(name))
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	List<Widget> getInventorySlots(String name)
	{
		List<Widget> widgets = new ArrayList<>();
		if (isTabOpen(WidgetInfo.INVENTORY))
		{
			Widget widget = client.getWidget(15, 3);
			if (widget != null)
			{
				Widget[] children = widget.getChildren();
				assert children != null;
				for (Widget child : children)
				{
					if (child.getName()
							.contains(name))
					{
						widgets.add(child);
					}
				}
			}
		}
		return widgets.size() > 0 ? widgets : null;
	}

	Widget getFirstInventorySlot(String name)
	{
		if (isTabOpen(WidgetInfo.INVENTORY))
		{
			Widget widget = client.getWidget(15, 3);
			if (widget != null)
			{
				Widget[] children = widget.getChildren();
				assert children != null;
				for (Widget child : children)
				{
					if (child.getName()
							.contains(name))
					{
						return child;
					}
				}
			}
		}
		return null;
	}

	WidgetItem getFirstInventorySlotContains(String name)
	{
		return new InventoryWidgetItemQuery()
				.filter(item -> item.getWidget()
						.getName()
						.contains(name))
				.result(client)
				.first();
	}

	WidgetItem getRandomInventorySlotContains(String name)
	{
		List<WidgetItem> list = new InventoryWidgetItemQuery()
				.filter(item -> item.getWidget()
						.getName()
						.contains(name))
				.result(client)
				.list;
		return list.get(new Random().nextInt(list.size()));
	}

	List<WidgetItem> getInventorySlotsContains(String name)
	{
		return new InventoryWidgetItemQuery()
				.filter(item -> item.getWidget()
						.getName()
						.contains(name))
				.result(client)
				.list;
	}

	List<WidgetItem> getInventorySlots(int... ids)
	{
		return new InventoryWidgetItemQuery()
				.idEquals(ids)
				.result(client)
				.list;
	}

	WidgetItem getRandomInventorySlot(int... ids)
	{
		ArrayList<WidgetItem> list = new InventoryWidgetItemQuery()
				.idEquals(ids)
				.result(client)
				.list;
		return list.get(new Random().nextInt(list.size()));
	}

	WidgetItem getFirstInventorySlot(int... ids)
	{
		return new InventoryWidgetItemQuery()
				.idEquals(ids)
				.result(client)
				.first();
	}

	public void sendChatMessage(String pattern, Object... arguments)
	{
		sendChatMessage(MessageFormat.format(pattern, arguments));
	}

	private String messageArgs(String pattern, Object... arguments)
	{
		return MessageFormat.format(pattern, arguments);
	}

	private QueuedMessage toChatMessage(String pattern, Object... arguments)
	{
		return QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage(MessageFormat.format(pattern, arguments))
				.build();
	}

	public void sendChatMessage(QueuedMessage message)
	{
		assert chatMessageManager != null;

		chatMessageManager.queue(message);
	}

	public void sendChatMessage(String chatMessage)
	{
		assert chatMessageManager != null;

		final String message = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage)
				.build();

		chatMessageManager.queue(
				QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(message)
						.build());
	}

}
