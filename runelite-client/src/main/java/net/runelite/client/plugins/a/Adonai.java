package net.runelite.client.plugins.a;

import net.runelite.api.Client;
import net.runelite.client.chat.ChatMessageManager;

public class Adonai
{
	public static Client client;
	public static ChatMessageManager chatManager;
	public static Keyboard keyboard;

	public static void initializeClient(Client client)
	{
		Adonai.client = client;
	}

	public static boolean isNullInitializeClient(Client client)
	{
		if (Adonai.client == null)
		{
			Adonai.client = client;
			return true;
		}
		return false;
	}

	public static void initializeChatMessageManager(ChatMessageManager chatMessageManager)
	{
		Adonai.chatManager = chatMessageManager;
	}

	public static boolean isNullInitializeChatMessageManager(ChatMessageManager chatMessageManager)
	{
		if (Adonai.chatManager == null)
		{
			initializeChatMessageManager(chatMessageManager);
			return true;
		}
		return false;
	}

	public static boolean isNullInitializeKeyboard(Keyboard keyboard)
	{
		if (Adonai.keyboard == null)
		{
			initializeKeyboard(keyboard);
			return true;
		}
		return false;
	}

	public static void initializeKeyboard(Keyboard keyboard)
	{
		Adonai.keyboard = keyboard;
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
	public static boolean isKeyboardManagerInitialized()
	{
		return keyboard != null;
	}
}
