package net.runelite.client.plugins.a.toolbox;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

public class Serialize
{
	public static byte[] convertToHashableByteArray(Serializable obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] byteOutput = null;

		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			byteOutput = bos.toByteArray();
		} catch (IOException io) {
			String stringed = obj.toString();
			byteOutput = stringed.getBytes();
		} finally {
			try {
				if(out != null) { out.close(); }
			} catch(IOException io) {
				io.printStackTrace();
			}

			try {
				bos.close();
			} catch(IOException io) {
				io.printStackTrace();
			}
		}

		return byteOutput;
	}

	/*
	 * Calculate checksum of a File using MD5 algorithm
	 */
	public static String createHash(Serializable obj){
		String checksum = org.apache.commons.codec.digest.DigestUtils.md5Hex(String.valueOf(obj));
		return checksum;
	}

}
