package org.caudexorigo.io;

import java.io.IOException;
import java.io.Writer;

public class NullOutputWriter extends Writer
{

	public void write(char[] cbuf) throws IOException
	{
	}

	public void write(int c) throws IOException
	{
	}

	public void write(String str, int off, int len) throws IOException
	{
	}

	public void write(String str) throws IOException
	{
	}

	public void write(char[] cbuf, int off, int len) throws IOException
	{
	}

	@Override
	public void flush() throws IOException
	{

	}

	@Override
	public void close() throws IOException
	{

	}

}
