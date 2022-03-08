/*
 * Copyright (c) 2019, Hydrox6 <ikada@protonmail.ch>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.adonaifisher;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.client.plugins.adonaifisher.AdonaiFisherConfig;

import java.util.Map;
import java.util.function.Predicate;

enum FishIdentification
{
	// Other
	TROUT(Type.FISH, "Raw trout", "T", ItemID.RAW_TROUT), // 335
	SALMON(Type.FISH, "Raw salmon", "S", ItemID.RAW_SALMON), // 331
	LEAPING_SALMON(Type.FISH, "L. Slmn", "LSL", ItemID.LEAPING_SALMON), // 11330
	LEAPING_STURGEON(Type.FISH, "L. Strgn", "LST", ItemID.LEAPING_STURGEON), // 11332
	LEAPING_TROUT(Type.FISH, "L. Trt", "LT", ItemID.LEAPING_TROUT), // 11328

	ROD(Type.ITEMS, "Fly rod", "FR", ItemID.FLY_FISHING_ROD), // 309
	BARB_ROD(Type.ITEMS, "Barb Rod", "BR", ItemID.BARBARIAN_ROD), // 11323
	HARPOON(Type.ITEMS, "Barb Rod", "HRP", ItemID.HARPOON), // 311
	LOBSTER_POT(Type.ITEMS, "Barb Rod", "LP", ItemID.LOBSTER_POT), // 301
	BIG_NET(Type.ITEMS, "Barb Rod", "BN", ItemID.BIG_FISHING_NET), // 305
	FEATHER(Type.ITEMS, "Feathers", "FE", ItemID.FEATHER); // 314

	final Type type;
	final String medName;
	final String shortName;
	final int[] itemIDs;

	FishIdentification(Type type, String medName, String shortName, int... ids)
	{
		this.type = type;
		this.medName = medName;
		this.shortName = shortName;
		this.itemIDs = ids;
	}

	private static final Map<Integer, FishIdentification> itemIdentifications;

	static
	{
		ImmutableMap.Builder<Integer, FishIdentification> builder = new ImmutableMap.Builder<>();

		for (FishIdentification i : values())
		{
			for (int id : i.itemIDs)
			{
				builder.put(id, i);
			}
		}

		itemIdentifications = builder.build();
	}

	static FishIdentification get(int id)
	{
		return itemIdentifications.get(id);
	}

	@AllArgsConstructor
	enum Type
	{
		FISH(AdonaiFisherConfig::showFish),
		ITEMS(AdonaiFisherConfig::showItems);

		final Predicate<AdonaiFisherConfig> enabled;
	}
}
