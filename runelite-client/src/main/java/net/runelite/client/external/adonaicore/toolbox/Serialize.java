package net.runelite.client.external.adonaicore.toolbox;

import java.io.*;

public class Serialize
{
	public static byte[] convertToHashableByteArray(Serializable obj)
	{
		ByteArrayOutputStream bos        = new ByteArrayOutputStream();
		ObjectOutput          out        = null;
		byte[]                byteOutput = null;

		try
		{
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			byteOutput = bos.toByteArray();
		}
		catch (IOException io)
		{
			String stringed = obj.toString();
			byteOutput = stringed.getBytes();
		}
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
			}
			catch (IOException io)
			{
				io.printStackTrace();
			}

			try
			{
				bos.close();
			}
			catch (IOException io)
			{
				io.printStackTrace();
			}
		}

		return byteOutput;
	}

	/*
	 * Calculate checksum of a File using MD5 algorithm
	 */
	public static String createHash(Serializable obj)
	{
		return createHash((Object) obj);
	}

	/*
	 * Calculate checksum of a File using MD5 algorithm
	 */
	public static String createHash(Object obj)
	{
		return org.apache.commons.codec.digest.DigestUtils.md5Hex(String.valueOf(obj));
	}

	public static boolean isHashEqual(Object obj, String hash)
	{
		return createHash(obj).equals(hash);
	}

	public static boolean isNewObject(Object obj, String hash)
	{
		return !isHashEqual(obj, hash);
	}


	private Object object;
	private Serializable serializable;
	private String hash;

	public Serialize(Object object)
	{
		this.object = object;
		this.serializable = (Serializable) object;
		this.hash = createHash(object);
	}

	public Serialize(Serializable serializable)
	{
		this.object = serializable;
		this.serializable = serializable;
		this.hash = createHash(serializable);
	}

	public boolean isEquals(Object newObject)
	{
		return isHashEqual(newObject, hash);
	}

	public boolean isNew(Object newObject)
	{
		return !isHashEqual(newObject, hash);
	}

	public void setNew(Object newObject)
	{
		this.object = newObject;
		this.serializable = (Serializable) newObject;
		this.hash = createHash(newObject);
	}

}
