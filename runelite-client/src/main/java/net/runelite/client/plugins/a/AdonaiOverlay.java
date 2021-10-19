package net.runelite.client.plugins.a;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class AdonaiOverlay extends Overlay
{

	private final Client client;
	private final AdonaiPlugin plugin;

	@Inject
	private AdonaiOverlay(Client client, AdonaiPlugin plugin)
	{
		this.client = client;
		this.plugin = plugin;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(OverlayPriority.HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (plugin.getMinimapLocation() != null)
		{
			OverlayUtil.renderMinimapRect(client, graphics, plugin.getMinimapLocation(), 8, 8, Color.BLUE);
		}
		return null;
	}
}
