package org.caudexorigo.jpt;

public class JptNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 0x7d868d88d9b87081L;

	public JptNotFoundException()
	{
	}

	public JptNotFoundException(String message)
	{
		super(message);
	}

	public JptNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public JptNotFoundException(Throwable cause)
	{
		super(cause);
	}
}