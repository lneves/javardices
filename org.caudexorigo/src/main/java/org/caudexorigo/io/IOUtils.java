package org.caudexorigo.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class IOUtils
{
	private static final Charset UTF8 = Charset.forName("utf-8");

	public static String toString(InputStream in) throws IOException
	{
		return toString(in, UTF8);
	}

	public static String toString(InputStream in, String encoding) throws IOException
	{
		Charset cs = Charset.forName(encoding);
		return toString(in, cs);
	}

	public static String toString(InputStream in, Charset cs) throws IOException
	{
		StringBuilder out = new StringBuilder();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;)
		{
			out.append(new String(b, 0, n, cs));
		}
		return out.toString();
	}

	public static byte[] toByteArray(InputStream in) throws IOException
	{
		UnsynchronizedByteArrayOutputStream out = new UnsynchronizedByteArrayOutputStream();

		int b;
		while ((b = in.read()) > -1)
		{
			out.write(b);
		}

		byte[] buf = out.toByteArray();
		out.close();
		return buf;
	}
}