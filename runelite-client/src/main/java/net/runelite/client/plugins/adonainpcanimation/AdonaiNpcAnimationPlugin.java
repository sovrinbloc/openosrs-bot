package net.runelite.client.plugins.adonainpcanimation;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.adonaifightcaves.FightCaveWavesPlugin;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Objects;

@PluginDescriptor(
		name = "Adonai NPC Information",
		description = "Get Spawn and Animation Information on NPC",
		enabledByDefault = false
)
@Slf4j
public class AdonaiNpcAnimationPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ChatMessageManager chatMessageManager;

	boolean jadSpawned = false;

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		final NPC npc = event.getNpc();
		sendChatMessage(MessageFormat.format("Name: {0} spawned.", npc.getName()));


	}

	@Subscribe
	public void onActorDeath(ActorDeath event)
	{
		final Actor actor = event.getActor();

		if (actor != client.getLocalPlayer() )
		{
			sendChatMessage(MessageFormat.format("Name: {0} dead.", actor.getName()));
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		final Actor actor = event.getActor();

		if (actor.getInteracting() == client.getLocalPlayer() || (!(actor instanceof NPC)) || actor.getAnimation() == -1)
		{
			return;
		}

		sendChatMessage(MessageFormat.format("Name: {0}, Animation: {1}.", actor.getName(), actor.getAnimation() ));
	}


	public void sendChatMessage(String chatMessage)
	{
		assert chatMessageManager != null;

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
