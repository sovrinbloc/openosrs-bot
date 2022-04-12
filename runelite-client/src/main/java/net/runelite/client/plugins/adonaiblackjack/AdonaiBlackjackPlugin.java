package net.runelite.client.plugins.adonaiblackjack;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MessageNode;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.QuantityFormatter;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
		name = "Adonai Blackjack",
		description = "Shows useful information in the chat for when to knock out, when to thieve, and when to stop.",
		tags = {"adonai", "thieving", "blackjack", "menaphite", "thieve", "pickpocket", "lure", "stun", "knock-out"}
)

@Slf4j
public class AdonaiBlackjackPlugin extends Plugin
{
	private static final String FAIL_MESSAGE = "Your blow only glances off the bandit's head.";
	private static final String SUCCESS_MESSAGE = "You smack the bandit over the head and render them unconscious.";

	@Inject
	private Client client;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		final String message = event.getMessage();
		if (message.contains(FAIL_MESSAGE))
		{

			final ChatMessageBuilder builder = new ChatMessageBuilder()
					.append(ChatColorType.HIGHLIGHT)
					.append("You have failed. This is just what you do. Get up on that horse and go and do this again.")
					.append("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
					.append(ChatColorType.NORMAL);

			chatMessageManager.queue(QueuedMessage.builder()
					.type(ChatMessageType.ITEM_EXAMINE)
					.runeLiteFormattedMessage(builder.build())
					.build());
		}


		client.refreshChat();
	}
}
