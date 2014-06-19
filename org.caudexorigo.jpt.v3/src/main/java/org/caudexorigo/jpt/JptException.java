package org.caudexorigo.jpt;

public class JptException extends RuntimeException
{
	private static final long serialVersionUID = 0x9349f02d300d8d5fL;

	public JptException()
	{
	}

	public JptException(String message)
	{
		super(message);
	}

	public JptException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public JptException(Throwable cause)
	{
		super(cause);
	}
}