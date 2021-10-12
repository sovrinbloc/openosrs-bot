package net.runelite.client.plugins.a.utils;

import net.runelite.api.GameState;
import net.runelite.api.events.FocusChanged;
import net.runelite.client.plugins.a.Adonai;

import static net.runelite.api.GameState.HOPPING;
import static net.runelite.api.GameState.LOGGED_IN;

public class GameStatus
{

	static boolean loggedIn;

	public static boolean isLoggedIn()
	{
		GameState gameState = Adonai.client.getGameState();
		loggedIn = gameState == LOGGED_IN || gameState == HOPPING;
		return loggedIn;
	}

	public static boolean isFocused(FocusChanged event)
	{
		return event.isFocused();
	}
}
