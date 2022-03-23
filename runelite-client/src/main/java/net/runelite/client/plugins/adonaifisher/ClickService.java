package net.runelite.client.plugins.adonaifisher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ClickService
{
	// left clicks
	private static String TRACK_URL = "http://0.0.0.0:4567/track/";
	private static String CLICK_URL = "http://0.0.0.0:4567/click/";
	private static String CLICK_FORCE_URL = "http://0.0.0.0:4567/click/force/";

	// right clicks
	private static String RIGHT_TRACK_URL = "http://0.0.0.0:4567/righttrack/";
	private static String RIGHT_CLICK_URL = "http://0.0.0.0:4567/rightclick/";
	private static String RIGHT_CLICK_FORCE_URL = "http://0.0.0.0:4567/rightclick/force/";

	private static String MOVE_URL = "http://0.0.0.0:4567/move/";
	private static String TYPE_URL = "http://0.0.0.0:4567/type";
	private static String PRESS_URL = "http://0.0.0.0:4567/key/";

	private static String getTrackUrl(int x, int y)
	{
		return TRACK_URL + x + "/" + y;
	}

	private static String getClickUrl(int x, int y)
	{
		return CLICK_URL + x + "/" + y;
	}

	private static String getForceClickUrl(int x, int y)
	{
		return CLICK_FORCE_URL + x + "/" + y;
	}

	private static String getRightTrackUrl(int x, int y)
	{
		return RIGHT_TRACK_URL + x + "/" + y;
	}

	private static String getRightClickUrl(int x, int y)
	{
		return RIGHT_CLICK_URL + x + "/" + y;
	}

	private static String getRightForceClickUrl(int x, int y)
	{
		return RIGHT_CLICK_FORCE_URL + x + "/" + y;
	}

	private static String getMoveUrl(int x, int y)
	{
		return MOVE_URL + x + "/" + y;
	}

	private static String getPressUrl(char key)
	{
		return PRESS_URL + key;
	}

	private static String getTypeUrl(String text)
	{
		return TYPE_URL + text;
	}

	public static void track(int x, int y)
			throws IOException
	{
		sendToServer(getTrackUrl(x, y));
	}

	public static void click(int x, int y)
			throws IOException
	{
		sendToServer(getClickUrl(x, y));
	}

	public static void clickForce(int x, int y)
			throws IOException
	{
		sendToServer(getForceClickUrl(x, y));
	}

	public static void rightTrack(int x, int y)
			throws IOException
	{
		sendToServer(getRightTrackUrl(x, y));
	}

	public static void rightClick(int x, int y)
			throws IOException
	{
		sendToServer(getRightClickUrl(x, y));
	}

	public static void rightClickForce(int x, int y)
			throws IOException
	{
		sendToServer(getRightForceClickUrl(x, y));
	}

	public static void move(int x, int y)
			throws IOException
	{
		sendToServer(getMoveUrl(x, y));
	}

	public static void press(char key)
			throws IOException
	{
		sendToServer(getPressUrl(key));
	}

	public static void type(String text)
			throws IOException
	{
		sendToServer(getTypeUrl(text));
	}

	public static void sendToServer(String urlString)
			throws IOException
	{
		URL url = new URL(urlString);
		InputStream is = url.openConnection()
				.getInputStream();
	}

	public static void connect(String urlString)
			throws IOException
	{
		URL url = new URL(urlString);
		URLConnection urlConnection = url.openConnection();
		urlConnection
				.connect();
	}



}
