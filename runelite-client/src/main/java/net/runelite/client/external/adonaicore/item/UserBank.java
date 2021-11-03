package net.runelite.client.external.adonaicore.item;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.queries.BankItemQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.adonaicore.Adonai;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

@Slf4j
public class UserBank
{
	private static Map<Item, BankItem> bankMap = new HashMap<>();

	public static void getItems()
	{
		// Compute bank prices using only the shown items so that we can show bank value during searches
		final Widget bankItemContainer = getBankContainerWidget();
		final ItemContainer bankContainer = Adonai.client.getItemContainer(InventoryID.BANK);
		final Widget[] children = bankItemContainer.getChildren();

		if (bankContainer != null && children != null)
		{
			log.debug("Computing bank price of {} items", bankContainer.size());
			log.info("Bank canvas location: {}", bankItemContainer.getCanvasLocation().toString());

			// The first components are the bank items, followed by tabs etc. There are always 816 components regardless
			// of bank size, but we only need to check up to the bank size.
			for (int i = 0; i < bankContainer.size(); ++i)
			{
				Widget child = children[i];
				if (child != null && !child.isSelfHidden() && child.getItemId() > -1)
				{
					bankMap.put(bankContainer.getItem(i), new BankItem(child, bankContainer.getItem(i)));
				}
			}
		}
	}

	public static List<Widget> getBankTabs()
	{
		Widget bankWidget = getBankContainerWidget();
		if (bankWidget.isSelfHidden())
		{
			return null;
		}
		Widget[] tabs = Adonai.client.getWidget(WidgetInfo.BANK_TAB_CONTAINER).getChildren();
		assert tabs != null;

		List<Widget> bankTabs = new ArrayList<>(List.of(tabs));

		return bankTabs.subList(0, (tabs.length / 2) - 1);
	}

	public static boolean isItemVisible(Item item)
	{
		Widget itemWidget = findItemWidget(item);
		if (itemWidget == null)
		{
			return false;
		}
		return Adonai.client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER).getBounds().contains(itemWidget.getBounds());
	}

	public static boolean itemExists(Item item)
	{
		return bankMap.containsKey(item);
	}

	enum ScrollDirection
	{
		UP,
		DOWN,
		NONE
	}

	public static ScrollDirection getScrollDirection(Item item)
	{
		double scrollDistance = getScrollDistance(item);
		return scrollDistance < 0 ? ScrollDirection.UP : scrollDistance > 0 ? ScrollDirection.DOWN : ScrollDirection.NONE;
	}

	public static double getScrollDistance(Item item)
	{
		java.awt.Rectangle bounds = Adonai.client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER).getBounds();
		java.awt.Rectangle cBounds = findItemWidget(item).getBounds();

		double scrollDistance = 0;
		if (bounds.getY() >= cBounds.getY())
		{
			scrollDistance = Math.abs(bounds.getY() - cBounds.getY()) * -1;
		}
		else if (bounds.getY() + bounds.getHeight() <= cBounds.getY() + cBounds.getHeight())
		{
			scrollDistance = bounds.getY() + bounds.getHeight() - cBounds.getY() - cBounds.getHeight();
		}
		return scrollDistance;

	}

	public static Widget findItemWidget(Item item)
	{
		getItems();
		if (bankMap.containsKey(item))
		{
			return bankMap.get(item).getItemWidget();
		}
		return null;
	}

	@Nullable
	private static Widget getBankContainerWidget()
	{
		return Adonai.client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
	}


	private Item[] getBankItems()
	{
		final ItemContainer itemContainer = Adonai.client.getItemContainer(InventoryID.BANK);

		if (itemContainer == null)
		{
			return null;
		}

		return itemContainer.getItems();
	}


	public List<WidgetItem> getBankItems(int... itemIDs)
	{
		assert Adonai.client.isClientThread();

		return new BankItemQuery()
				.idEquals(itemIDs)
				.result(Adonai.client)
				.list;
	}

	public List<WidgetItem> getBankItems(String... names)
	{
		assert Adonai.client.isClientThread();

		ArrayList<WidgetItem> items = new BankItemQuery()
				.result(Adonai.client)
				.list;

		new ArrayList<>(Arrays.asList(names)).forEach(inventoryItem ->
				items.removeIf(item -> item.getWidget().getName().equals(inventoryItem)));

		return items;
	}

	public List<WidgetItem> getBankItems(Predicate<WidgetItem> filter)
	{
		assert Adonai.client.isClientThread();

		return new BankItemQuery()
				.filter(filter)
				.result(Adonai.client)
				.list;
	}
}
