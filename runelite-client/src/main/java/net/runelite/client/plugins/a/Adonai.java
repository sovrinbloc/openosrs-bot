package net.runelite.client.plugins.a;

import net.runelite.api.Client;
import net.runelite.client.chat.ChatMessageManager;

public class Adonai
{
	public static Client client;
	public static ChatMessageManager chatManager;

	public static void initializeClient(Client client)
	{
		Adonai.client = client;
	}

	public static void initializeChatMessageManager(ChatMessageManager chatMessageManager)
	{
		Adonai.chatManager = chatMessageManager;
	}

	/**
	 * Checks to see if the client has been initialized to use all of the features within.
	 */
	public static boolean isClientInitialized()
	{
		return client != null;
	}

	/**
	 * Checks to see if the chat manager has been initialized for the messages features.
	 */
	public static boolean isChatManagerInitialized()
	{
		return chatManager != null;
	}
}
