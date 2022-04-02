package net.runelite.client.plugins.adonaicore;

import net.runelite.client.external.adonaicore.toolbox.Calculations;

public class Randoms
{
	public static StringBuilder moveCamera()
	{
		char cameraMovement;

		StringBuilder outcome = new StringBuilder();
		int numberPressed = ExtUtils.random(5, 20);
		switch (Calculations.random(1, 5))
		{
			case 1:
				cameraMovement = Keyboard.LEFT_ARROW_KEY;
				break;
			case 2:
				cameraMovement = Keyboard.RIGHT_ARROW_KEY;
				break;
			case 3:
				cameraMovement = Keyboard.UP_ARROW_KEY;
				break;
			default:
				cameraMovement = Keyboard.DOWN_ARROW_KEY;
				break;
		}
		for (int i = 0; i < numberPressed; i++)
		{
			outcome.append(cameraMovement);
		}
		return outcome;
	}
}
