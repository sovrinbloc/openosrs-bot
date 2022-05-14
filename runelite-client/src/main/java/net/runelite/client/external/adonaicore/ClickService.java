package net.runelite.client.external.adonaicore;

import net.runelite.api.Point;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ClickService implements Auto
{
	private static final String TRACK_URL = "http://0.0.0.0:4567/track/";
	private static final String CLICK_URL = "http://0.0.0.0:4567/click/";
	private static final String MOVE_URL = "http://0.0.0.0:4567/move/";
	private static final String TYPE_URL = "http://0.0.0.0:4567/type";
	private static final String PRESS_URL = "http://0.0.0.0:4567/key/";
	private static final String HELLO_URL = "http://0.0.0.0:4567/hello/";

	private static String getTrackUrl(int x, int y)
	{
		return TRACK_URL + x + "/" + y;
	}

	private static String getClickUrl(int x, int y)
	{
		return CLICK_URL + x + "/" + y;
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

	private static String getHello()
	{
		return HELLO_URL;
	}

	public static int track(Point pt)
			throws IOException
	{
		return track(pt.getX(), pt.getY());
	}

	public static int track(int x, int y)
			throws IOException
	{
		return execute(getTrackUrl(x, y));
	}

	public static int click(Point pt)
			throws IOException
	{
		return click(pt.getX(), pt.getY());
	}

	public static int click(int x, int y)
			throws IOException
	{
		return execute(getClickUrl(x, y));
	}

	public static int move(Point pt)
			throws IOException
	{
		return move(pt.getX(), pt.getY());
	}

	public static int move(int x, int y)
			throws IOException
	{
		return execute(getMoveUrl(x, y));
	}

	public static int press(char key)
			throws IOException
	{
		return execute(getPressUrl(key));
	}

	public static int type(String text)
			throws IOException
	{
		return execute(getTypeUrl(text));
	}

	public static boolean hello()
			throws IOException
	{
		int execute = execute(getHello());
		return execute >= 200 && execute < 300;
	}

	public static int execute(String urlString)
			throws IOException
	{
		URL               url               = new URL(urlString);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		return httpURLConnection.getResponseCode();
	}

	public static void connect(String urlString)
			throws IOException
	{
		URL           url           = new URL(urlString);
		URLConnection urlConnection = url.openConnection();
		urlConnection.connect();
	}


}
