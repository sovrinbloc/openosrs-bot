package net.runelite.client.external.adonaicore.item;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
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


	/**
	 * Gets a snapshot of your inventory
	 */
	private void takeInventorySnapshot()
	{
		Multiset<Integer> inventorySnapshot;
		final ItemContainer itemContainer = Adonai.client.getItemContainer(InventoryID.INVENTORY);
		if (itemContainer != null)
		{
			inventorySnapshot = HashMultiset.create();
			Arrays.stream(itemContainer.getItems())
					.forEach(item -> inventorySnapshot.add(item.getId(), item.getQuantity()));
		}
	}

}
