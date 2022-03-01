/*
<<<<<<< HEAD
 * Copyright (c) 2021, Adam <Adam@sigterm.info>
=======
<<<<<<< HEAD:runelite-client/src/main/java/net/runelite/client/external/adonaicore/menu/examine/ExamineType.java
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
=======
 * Copyright (c) 2021, Adam <Adam@sigterm.info>
>>>>>>> fba6f9b58bb7fa460262280f02db9641486614bf:runelite-client/src/main/java/net/runelite/client/hiscore/Skill.java
>>>>>>> fba6f9b58bb7fa460262280f02db9641486614bf
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

//package net.runelite.client.external.adonaicore.menu.examine;

//public enum ExamineType
//{
//	ITEM,
//	ITEM_BANK_EQ,
//	NPC,
//	OBJECT;
//}
package net.runelite.client.hiscore;

import lombok.Value;

@Value
public class Skill
{
	int rank;
	int level;
	long experience;
}
