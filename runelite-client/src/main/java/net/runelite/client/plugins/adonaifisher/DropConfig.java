package net.runelite.client.plugins.adonaifisher;

import net.runelite.client.external.adonai.ExtUtils;

import java.util.ArrayList;
import java.util.List;

public class DropConfig
{

	// flags for configuration of bot
	public static boolean useNames = false;
	public static boolean findSpotByAction = false;
	public static boolean dropAllExcept = true;

	// storage variables for content
	public static String spotName = "";
	public static List<String> spotActions = new ArrayList<>();
	public static List<Integer> inventoryIds = new ArrayList<>();
	public static List<String> inventoryNames = new ArrayList<>();
//	public static ExtUtils.Inventory.InventoryDropMode dropMode = ExtUtils.Inventory.InventoryDropMode.LEFT_RIGHT_TOP_DOWN;
	public static int dropPause = 200;
	public static int dropErrorPause = 3000;

	public static List<Integer> dropItems = new ArrayList<>()
	{
		{
			add(FishIdentification.LEAPING_SALMON.itemIDs[0]);
			add(FishIdentification.LEAPING_TROUT.itemIDs[0]);
			add(FishIdentification.LEAPING_STURGEON.itemIDs[0]);
			add(FishIdentification.SALMON.itemIDs[0]);
			add(FishIdentification.TROUT.itemIDs[0]);
		}
	};
	public static List<Integer> keepItems = new ArrayList<>()
	{
		{
			add(FishIdentification.ROD.itemIDs[0]);
			add(FishIdentification.LOBSTER_POT.itemIDs[0]);
			add(FishIdentification.BIG_NET.itemIDs[0]);
			add(FishIdentification.HARPOON.itemIDs[0]);
			add(FishIdentification.BARB_ROD.itemIDs[0]);
			add(FishIdentification.FEATHER.itemIDs[0]);
		}
	};
}
