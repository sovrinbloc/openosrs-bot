package net.runelite.client.plugins.a.item;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.a.Adonai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class UserEquippedItems
{

	public List<Widget> getEquippedItems(int[] itemIds)
	{
		assert Adonai.client.isClientThread();

		Widget equipmentWidget = Adonai.client.getWidget(WidgetInfo.EQUIPMENT);

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

	public List<Widget> getEquippedItems(String[] names)
	{
		assert Adonai.client.isClientThread();

		Widget equipmentWidget = Adonai.client.getWidget(WidgetInfo.EQUIPMENT);

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

	public List<Widget> getEquippedItems()
	{
		assert Adonai.client.isClientThread();

		Widget equipmentWidget = Adonai.client.getWidget(WidgetInfo.EQUIPMENT);

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
