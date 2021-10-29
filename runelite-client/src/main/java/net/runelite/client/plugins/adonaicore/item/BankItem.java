package net.runelite.client.plugins.adonaicore.item;

import net.runelite.api.Item;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;

public class BankItem
{
	private final Widget itemWidget;
	private final Item item;
	private final int quantity;
	private final Point canvasLocation;

	public Point getCanvasLocation()
	{
		return canvasLocation;
	}

	BankItem(Widget itemWidget, Item item)
	{
		this.item = item;
		this.itemWidget = itemWidget;
		this.quantity = itemWidget.getItemQuantity();
		this.canvasLocation = itemWidget.getCanvasLocation();
	}

	public Widget getItemWidget()
	{
		return itemWidget;
	}

	public Item getItem()
	{
		return item;
	}

	public int getQuantity()
	{
		return quantity;
	}
}
