package net.runelite.client.plugins.a;

import net.runelite.api.GameState;

import static net.runelite.api.GameState.HOPPING;
import static net.runelite.api.GameState.LOGGED_IN;

public class StatusUtils
{

	static boolean loggedIn;

	public static boolean isLoggedIn()
	{
		GameState gameState = Adonai.client.getGameState();
		loggedIn = gameState == LOGGED_IN || gameState == HOPPING;
		return loggedIn;
	}
}
