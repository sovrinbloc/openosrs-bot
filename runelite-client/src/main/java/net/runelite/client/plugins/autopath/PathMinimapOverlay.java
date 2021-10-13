package net.runelite.client.plugins.autopath;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.plugins.a.ExtUtils;
import net.runelite.client.plugins.a.utils.Screen;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;

public class PathMinimapOverlay extends Overlay
{
	private static final int TILE_WIDTH = 4;
	private static final int TILE_HEIGHT = 4;

	private final Client client;
	private final AutoPathPlugin plugin;
	private final AutoPathConfig config;

	@Inject
	private PathMinimapOverlay(Client client, AutoPathPlugin plugin, AutoPathConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.LOW);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	public static LocalPoint furthest = null;
	public static boolean set = false;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.drawMinimap())
		{
			return null;
		}

		if (plugin.path != null)
		{
			highlightFurthestTileFromEnd(graphics);
//			highlightFurthestTileFromStart(graphics);
		}

		return null;
	}

	private void highlightFurthestTileFromStart(Graphics2D graphics)
	{
		// from current to end
		for (WorldPoint point : plugin.path)
		{
			if (point.getPlane() != client.getPlane())
			{
				continue;
			}

			ExtUtils u = new ExtUtils(client);
			LocalPoint localPoint = LocalPoint.fromWorld(client, point);
			if (localPoint != null)
			{
				Point minimapPoint = Perspective.localToMinimap(client, localPoint);
				if (minimapPoint != null)
				{
					if (Screen.isOnScreen(minimapPoint))
					{
						LocalPoint lpUser = client.getLocalPlayer().getLocalLocation();
						if (furthest == null || point.equals(plugin.path.get(plugin.path.size() - 1)) || furthest.distanceTo(lpUser) < localPoint.distanceTo(lpUser))
						{
							furthest = localPoint;
						}
					}

				}
			}
			drawOnMinimap(graphics, point);
		}
	}

	private void highlightFurthestTileFromEnd(Graphics2D graphics)
	{
		for (int i = plugin.path.size() - 1; i >= 0; i--)
		{
			WorldPoint point = plugin.path.get(i);
			if (point.getPlane() != client.getPlane())
			{
				continue;
			}

			ExtUtils u = new ExtUtils(client);
			LocalPoint localPoint = LocalPoint.fromWorld(client, point);
			if (localPoint != null)
			{
				Point minimapPoint = Perspective.localToMinimap(client, localPoint);
				if (minimapPoint != null)
				{
					if (Screen.isOnScreen(minimapPoint))
					{
						LocalPoint lpUser = client.getLocalPlayer().getLocalLocation();

						// end point is on screen
						if (plugin.end.equals(point))
						{
							furthest = localPoint;
							plugin.endInSight = true;
						} else if (furthest == null || ((furthest.distanceTo(lpUser) < localPoint.distanceTo(lpUser)) && (Screen.isOnScreen(LocalPoint.fromWorld(client, plugin.end), plugin.end.getPlane()))))
						{
							furthest = localPoint;
							plugin.endInSight = false;
						}
					}

				}
			}
			drawOnMinimap(graphics, point);
		}
	}

	private void drawOnMinimap(Graphics2D graphics, WorldPoint point)
	{
		WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();

		if (point.distanceTo(playerLocation) >= 50)
		{
			return;
		}

		LocalPoint lp = LocalPoint.fromWorld(client, point);

		if (lp == null)
		{
			return;
		}

		Point posOnMinimap = Perspective.localToMinimap(client, lp);

		if (posOnMinimap == null)
		{
			return;
		}

		if (lp.equals(furthest))
		{
			renderMinimapRect(client, graphics, posOnMinimap, TILE_WIDTH, TILE_HEIGHT, new Color(ExtUtils.random(0, 255), ExtUtils.random(0, 255), ExtUtils.random(0, 255), 255));
			return;
		}

		renderMinimapRect(client, graphics, posOnMinimap, TILE_WIDTH, TILE_HEIGHT, new Color(255, 255, 0, 255));
	}

	public static void renderMinimapRect(Client client, Graphics2D graphics, Point center, int width, int height, Color color)
	{
		double angle = client.getMapAngle() * Math.PI / 1024.0d;

		graphics.setColor(color);
		graphics.rotate(angle, center.getX(), center.getY());
		graphics.fillRect(center.getX() - width / 2, center.getY() - height / 2, width, height);
		graphics.rotate(-angle, center.getX(), center.getY());
	}


	@Inject
	private ChatMessageManager chatMessageManager;

	private void sendChatMessage(String chatMessage)
	{
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
