/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
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

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.AnimationID;

import java.time.Instant;
import java.util.Set;

class FishingSession
{
	static final String FISHING_SPOT = "Fishing spot";
	static final Set<Integer> FISHING_ANIMATIONS = ImmutableSet.of(
			AnimationID.FISHING_BARBTAIL_HARPOON,
			AnimationID.FISHING_BAREHAND,
			AnimationID.FISHING_BAREHAND_CAUGHT_SHARK_1,
			AnimationID.FISHING_BAREHAND_CAUGHT_SHARK_2,
			AnimationID.FISHING_BAREHAND_CAUGHT_SWORDFISH_1,
			AnimationID.FISHING_BAREHAND_CAUGHT_SWORDFISH_2,
			AnimationID.FISHING_BAREHAND_CAUGHT_TUNA_1,
			AnimationID.FISHING_BAREHAND_CAUGHT_TUNA_2,
			AnimationID.FISHING_BAREHAND_WINDUP_1,
			AnimationID.FISHING_BAREHAND_WINDUP_2,
			AnimationID.FISHING_BIG_NET,
			AnimationID.FISHING_CAGE,
			AnimationID.FISHING_CRYSTAL_HARPOON,
			AnimationID.FISHING_DRAGON_HARPOON,
			AnimationID.FISHING_DRAGON_HARPOON_OR,
			AnimationID.FISHING_HARPOON,
			AnimationID.FISHING_INFERNAL_HARPOON,
			AnimationID.FISHING_TRAILBLAZER_HARPOON,
			AnimationID.FISHING_KARAMBWAN,
			AnimationID.FISHING_NET,
			AnimationID.FISHING_OILY_ROD,
			AnimationID.FISHING_POLE_CAST,
			AnimationID.FISHING_PEARL_ROD,
			AnimationID.FISHING_PEARL_FLY_ROD,
			AnimationID.FISHING_PEARL_BARBARIAN_ROD,
			AnimationID.FISHING_PEARL_ROD_2,
			AnimationID.FISHING_PEARL_FLY_ROD_2,
			AnimationID.FISHING_PEARL_BARBARIAN_ROD_2,
			AnimationID.FISHING_PEARL_OILY_ROD
	);
	@Getter
	@Setter
	private Instant lastFishCaught;

	enum PlayerState
	{
		FISHING,
		NOT_FISHING,
		INVENTORY_FULL,
		DROPPING_FISH,
		DONE_DROPPING_FISH,
		SHOULD_START_DROPPING,
		STARTING_SOFTWARE,
		SHOULD_START_FISHING,
		NOTHING

	}
}
