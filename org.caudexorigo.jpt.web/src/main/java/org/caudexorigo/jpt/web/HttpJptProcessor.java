package org.caudexorigo.jpt.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public interface HttpJptProcessor
{
	public void clearResponse();

	public String getHeader(String headerName);

	public InputStream getInputStream() throws IOException;

	public Method getMethod();

	public OutputStream getOutputStream() throws IOException;

	public String getParameter(String p_name);
	
	public List<String> getParameters(String name);

	public Map<String, List<String>> getParameters();

	public String getRequestPath();

	public Object getSessionValue(String attr_name);

	public Writer getWriter() throws IOException;

	public void include(String uri);

	public void setHeader(String headerName, String headerValue);

	public void setSessionValue(String attr_name, Object value);

	public void setStatus(int statusCode);
	
	public int getStatus();
	
	public InetSocketAddress getClientLocalAddress();
	
	public InetSocketAddress getClientRemoteAddress();
}
