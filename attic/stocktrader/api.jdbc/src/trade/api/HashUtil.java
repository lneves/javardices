package trade.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil
{
	private static MessageDigest getDigest()
	{
		try
		{
			return MessageDigest.getInstance("SHA-1");
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static final MessageDigest md = getDigest();

	private static String toHex(byte hash[])
	{
		StringBuilder buf = new StringBuilder(hash.length * 2);
		for (int i = 0; i < hash.length; i++)
		{
			int intVal = hash[i] & 0xff;
			if (intVal < 0x10)
			{
				// append a zero before a one digit hex
				// number to make it two digits.
				buf.append("0");
			}
			buf.append(Integer.toHexString(intVal));
		}
		return buf.toString();
	}

	public static String getHashedPassword(String pass, String salt)
	{
		try
		{
			String tmp = pass + salt;

			return toHex(md.digest(tmp.getBytes("utf-8")));
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}
}