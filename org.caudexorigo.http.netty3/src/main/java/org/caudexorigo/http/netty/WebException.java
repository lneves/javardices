package org.caudexorigo.http.netty;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class WebException extends RuntimeException
{
	private static final long serialVersionUID = -1902563445011490906L;

	private final int http_status_code;

	private final Map<String, String> properties = new HashMap<String, String>();

	public WebException(Throwable cause, int httpStatusCode)
	{
		super(cause);
		http_status_code = httpStatusCode;
	}

	public int getHttpStatusCode()
	{
		return http_status_code;
	}

	public void addProperty(String name, String value)
	{
		if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value))
		{
			properties.put(name, value);
		}
	}

	public String getProperty(String name)
	{
		if (StringUtils.isNotBlank(name))
		{
			return properties.get(name);
		}
		else
		{
			throw new IllegalArgumentException("Illegal name for a property name");
		}
	}
}