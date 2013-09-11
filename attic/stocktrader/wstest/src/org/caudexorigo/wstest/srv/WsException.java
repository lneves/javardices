package org.caudexorigo.wstest.srv;

public class WsException extends RuntimeException
{
	private static final long serialVersionUID = -1902563445011490906L;

	private final int http_status_code;

	public WsException(Throwable cause, int httpStatusCode)
	{
		super(cause);
		http_status_code = httpStatusCode;
	}

	public int getHttpStatusCode()
	{
		return http_status_code;
	}
}