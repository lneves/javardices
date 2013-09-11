package org.caudexorigo.jersey.netty.http.examples;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.InputStream;

import org.caudexorigo.Shutdown;

public class BenchImage
{
	private static final byte[] image = new byte[43];
	private static final ByteBuf buf = Unpooled.directBuffer(image.length);

	static
	{
		try
		{
			InputStream in = Bench.class.getResourceAsStream("bench.gif");
			int b;
			int i = 0;
			while ((b = in.read()) > -1)
			{
				image[i] = (byte) b;
				i++;
			}

			buf.writeBytes(image, 0, image.length);
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}

	public static byte[] getBytes()
	{
		return image;
	}

	public static ByteBuf getChannelBuffer()
	{
		return buf.copy();
	}
}