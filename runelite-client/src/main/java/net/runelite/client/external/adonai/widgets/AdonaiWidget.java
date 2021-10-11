package net.runelite.client.external.adonai.widgets;

import net.runelite.api.widgets.Widget;
//import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.a.Adonai;

public class AdonaiWidget
{
	net.runelite.api.widgets.Widget widget;
	AdonaiWidget info;

	public Widget getWidget(net.runelite.client.external.adonai.widgets.WidgetInfo widgetInfo)
	{
		return  Adonai.client.getWidget(widget.getId);
	}

}
