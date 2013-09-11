package org.caudexorigo.jpt;

import java.io.UnsupportedEncodingException;
import java.net.URI;

public class JptUtil
{
	private JptUtil()
	{
	}

	static char recycleCharBuff[] = new char[8192];

	public static byte[] asciiGetBytes(String buf)
	{
		int size = buf.length();
		byte bytebuf[] = new byte[size];
		char charBuff[] = (char[]) null;
		if (size < 8192)
			charBuff = recycleCharBuff;
		else
			charBuff = new char[size];
		buf.getChars(0, size, charBuff, 0);
		for (int i = 0; i < size; i++)
			bytebuf[i] = (byte) charBuff[i];
		return bytebuf;
	}

	public static byte[] getBytes(String buf, String encoding)
	{
		try
		{
			return buf.getBytes(encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JptException(e);
		}
	}

	public static URI resolvePath(String path)
	{
		String i_path = "";

		if (path.startsWith("file://"))
		{
			i_path = path;
		}
		else
		{
			i_path = "file://" + path;
		}
		return URI.create(i_path);
	}

	public static String removeDelims(String inputStr, String right_delim, String left_delim)
	{
		int r_delim_p_pos = inputStr.indexOf(right_delim);
		int l_delim_p_pos = inputStr.indexOf(left_delim, r_delim_p_pos + 1);
		if (r_delim_p_pos > 0)
			return inputStr.substring(r_delim_p_pos + 1, l_delim_p_pos).trim();
		else
			return inputStr;
	}

}
