package net.runelite.client.plugins.adonaicore.item;

import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;

import javax.annotation.Nullable;
import java.awt.*;

public class GameTileObject
{

	public GameTileObject(TileItem o, Tile tile, Client client)
	{
//		this.x = tile.getLocalLocation().getX();
//		this.y = tile.getLocalLocation().getY();
//		this.actions = o.getActions();
//		this.canvasLocation = LocalPoint.fromScene(tile.getSceneLocation().getX(), tile.getSceneLocation().getY());
//		this.canvasTilePoly = o.getCanvasTilePoly();
//		this.clickbox = o.getClickbox();
//		this.id = o.getId();
//		this.name = o.getName();
//		this.minimapLocation = o.getMinimapLocation();
//		this.y = o.getY();
//		this.plane = o.getPlane();
//		this.tile = tile;
//		this.tileObject = o;
	}

	public GameTileObject(TileObject o, Tile tile)
	{
		this.x = o.getX();
		this.actions = o.getActions();
		this.canvasLocation = o.getCanvasLocation();
		this.canvasTilePoly = o.getCanvasTilePoly();
		this.clickbox = o.getClickbox();
		this.id = o.getId();
		this.name = o.getName();
		this.minimapLocation = o.getMinimapLocation();
		this.y = o.getY();
		this.plane = o.getPlane();
		this.tile = tile;
		this.tileObject = o;
	}

	@Getter
	Tile tile;

	/**
	 * Gets the x-axis coordinate of the object in local context.
	 *
	 * @see LocalPoint
	 */
	@Getter
	int x;

	/**
	 * Gets the y-axis coordinate of the object in local context.
	 *
	 * @see LocalPoint
	 */
	@Getter
	int y;

	/**
	 * Gets the plane of the tile that the object is on.
	 */
	@Getter
	int plane;

	/**
	 * Gets the ID of the object.
	 *
	 * @see ObjectID
	 * @see NullObjectID
	 */
	@Getter
	int id;

	/**
	 * Calculates the position of the center of this tile on the canvas
	 */
	@Getter
	Point canvasLocation;

	/**
	 * Creates a polygon outlining the tile this object is on
	 */
	@Getter
	Polygon canvasTilePoly;

	/**
	 * Gets a point on the canvas of where this objects mini-map indicator
	 * should appear.
	 *
	 * @return mini-map location on canvas
	 */
	@Getter
	Point minimapLocation;

	/**
	 * Calculate the on-screen clickable area of the object.
	 */
	@Nullable
	@Getter
	Shape clickbox;

	/**
	 * Gets the name of the object
	 */
	@Getter
	String name;

	/**
	 * Gets the menu actions of the object
	 */
	@Getter
	String[] actions;

	@Getter
	private TileObject tileObject;

	@Getter
	private TileItem groundItem;

}
