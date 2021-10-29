package net.runelite.client.plugins.adonaicore.item;

import net.runelite.api.queries.InventoryWidgetItemQuery;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.adonaicore.Adonai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class UserInventory
{

	public static List<WidgetItem> getItems(int... itemIDs)
	{
		assert Adonai.client.isClientThread();

		return new InventoryWidgetItemQuery()
				.idEquals(itemIDs)
				.result(Adonai.client)
				.list;
	}

	public static List<WidgetItem> getItems(String... names)
	{
		assert Adonai.client.isClientThread();

		ArrayList<WidgetItem> items = new InventoryWidgetItemQuery()
				.result(Adonai.client)
				.list;

		new ArrayList<>(Arrays.asList(names)).forEach(inventoryItem ->
				items.removeIf(item -> item.getWidget()
						.getName()
						.equals(inventoryItem)));

		return items;
	}

	public static List<WidgetItem> getItems(Predicate<WidgetItem> filter)
	{
		assert Adonai.client.isClientThread();

		return new InventoryWidgetItemQuery()
				.filter(filter)
				.result(Adonai.client)
				.list;
	}

	public static void getItemLocations()
	{}

	public static boolean isFull()
	{
		assert Adonai.client != null;

		return Adonai.client.getWidget(WidgetInfo.INVENTORY)
				.getWidgetItems()
				.size() == 28;
	}


}
