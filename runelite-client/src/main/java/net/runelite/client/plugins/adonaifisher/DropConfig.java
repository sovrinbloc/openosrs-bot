package net.runelite.client.plugins.adonaifisher;

import java.util.ArrayList;
import java.util.List;

public class DropConfig
{
	public static List<Integer> dropItems = new ArrayList<>()
	{
		{
			add(FishIdentification.LEAPING_SALMON.itemIDs[0]);
			add(FishIdentification.LEAPING_TROUT.itemIDs[0]);
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
