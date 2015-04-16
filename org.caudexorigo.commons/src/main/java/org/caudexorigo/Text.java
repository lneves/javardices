package org.caudexorigo;

import java.io.IOException;

import org.caudexorigo.io.IOUtils;

public class Text
{
	public static final String get(String path)
	{
		try
		{
			return IOUtils.toString(Text.class.getResourceAsStream(path));
		}
		catch (IOException ioe)
		{
			throw new RuntimeException(ioe);
		}
	}
}
