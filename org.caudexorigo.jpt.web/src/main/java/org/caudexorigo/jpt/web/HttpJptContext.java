package org.caudexorigo.jpt.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.caudexorigo.jpt.BaseJptContext;

public class HttpJptContext extends BaseJptContext
{
	private final HttpJptProcessor _httpProcessor;

	public HttpJptContext(HttpJptProcessor httpProcessor, URI templateURI)
	{
		super(templateURI);
		_httpProcessor = httpProcessor;
	}

	public final void clearResponse()
	{
		_httpProcessor.clearResponse();
	}

	public final String getHeader(String headerName)
	{
		return _httpProcessor.getHeader(headerName);
	}

	public InputStream getInputStream() throws IOException
	{
		return _httpProcessor.getInputStream();
	}

	public final Method getMethod()
	{
		return _httpProcessor.getMethod();
	}

	public final OutputStream getOutputStream() throws IOException
	{
		return _httpProcessor.getOutputStream();
	}

	public List<String> getParameters(String p_name)
	{
		return _httpProcessor.getParameters(p_name);
	}

	public final String getParameter(String p_name)
	{
		return _httpProcessor.getParameter(p_name);
	}

	public Map<String, List<String>> getParameters()
	{
		return _httpProcessor.getParameters();
	}

	public final String getRequestPath()
	{
		return _httpProcessor.getRequestPath();
	}

	public final Object getSessionValue(String attr_name)
	{
		return _httpProcessor.getSessionValue(attr_name);
	}

	public final Writer getWriter() throws IOException
	{
		return _httpProcessor.getWriter();
	}

	public final void include(String uri)
	{
		_httpProcessor.include(uri);
	}

	public final void setHeader(String headerName, String headerValue)
	{
		_httpProcessor.setHeader(headerName, headerValue);
	}

	public final void setSessionValue(String attr_name, Object value)
	{
		_httpProcessor.setSessionValue(attr_name, value);
	}

	public final void setStatus(int statusCode)
	{
		_httpProcessor.setStatus(statusCode);
	}

	public final int getStatus()
	{
		return _httpProcessor.getStatus();
	}

	public InetSocketAddress getClientLocalAddress()
	{
		return _httpProcessor.getClientLocalAddress();
	}

	public InetSocketAddress getClientRemoteAddress()
	{
		return _httpProcessor.getClientRemoteAddress();
	}

	private static final int FOUND_STATUS_CODE = 302;
	private static final String LOCATION = "Location";

	public void redirect(String url)
	{
		setStatus(FOUND_STATUS_CODE);
		setHeader(LOCATION, url);
	}
}