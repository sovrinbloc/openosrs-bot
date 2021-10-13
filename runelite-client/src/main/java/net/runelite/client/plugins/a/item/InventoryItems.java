package net.runelite.client.plugins.a.item;

import net.runelite.api.queries.InventoryWidgetItemQuery;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.a.Adonai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class InventoryItems
{

	public List<WidgetItem> getItems(int... itemIDs)
	{
		assert Adonai.client.isClientThread();

		return new InventoryWidgetItemQuery()
				.idEquals(itemIDs)
				.result(Adonai.client)
				.list;
	}

	public List<WidgetItem> getItems(String... names)
	{
		assert Adonai.client.isClientThread();

		ArrayList<WidgetItem> items = new InventoryWidgetItemQuery()
				.result(Adonai.client)
				.list;

		new ArrayList<>(Arrays.asList(names)).forEach(inventoryItem ->
				items.removeIf(item -> item.getWidget().getName().equals(inventoryItem)));

		return items;
	}

	public List<WidgetItem> getItems(Predicate<WidgetItem> filter)
	{
		assert Adonai.client.isClientThread();

		return new InventoryWidgetItemQuery()
				.filter(filter)
				.result(Adonai.client)
				.list;
	}
}
