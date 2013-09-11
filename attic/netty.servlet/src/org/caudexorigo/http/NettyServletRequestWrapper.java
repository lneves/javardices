package org.caudexorigo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.HttpRequestWrapper;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;

public class NettyServletRequestWrapper implements HttpServletRequest
{
	private final ChannelHandlerContext netty_ctx;
	private final HttpRequestWrapper netty_request;
	private final Map<String, Object> request_attributes;
	private final HttpAction http_action;

	NettyServletRequestWrapper(HttpAction http_action, ChannelHandlerContext ctx, HttpRequestWrapper http_request)
	{
		this.http_action = http_action;
		this.netty_ctx = ctx;
		this.netty_request = http_request;
		request_attributes = new HashMap<String, Object>();

	}

	@Override
	public Object getAttribute(String name)
	{
		return request_attributes.get(name);
	}

	@Override
	public Enumeration<String> getAttributeNames()
	{
		return Collections.enumeration(request_attributes.keySet());
	}

	@Override
	public String getCharacterEncoding()
	{
		return netty_request.getCharacterEncoding();
	}

	@Override
	public int getContentLength()
	{
		return (int) netty_request.getContentLength();
	}

	@Override
	public String getContentType()
	{
		return netty_request.getHeader(HttpHeaders.Names.CONTENT_TYPE);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException
	{
		ChannelBuffer buf = netty_request.getContent();

		final ChannelBufferInputStream ch_id = new ChannelBufferInputStream(buf);

		return new ServletInputStream()
		{

			@Override
			public int read() throws IOException
			{
				return ch_id.read();
			}
		};
	}

	@Override
	public String getLocalAddr()
	{
		return netty_ctx.getChannel().getLocalAddress().toString();
	}

	@Override
	public String getLocalName()
	{
		return netty_ctx.getChannel().getLocalAddress().toString();
	}

	@Override
	public int getLocalPort()
	{
		return 0;
	}

	@Override
	public Locale getLocale()
	{
		return Locale.getDefault();
	}

	@Override
	public Enumeration<Locale> getLocales()
	{
		List<Locale> lst = Collections.emptyList();
		return Collections.enumeration(lst);
	}

	@Override
	public String getParameter(String name)
	{
		return netty_request.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap()
	{
		Map<String, String[]> map = new HashMap<String, String[]>();

		List<String> lst_param_names = Collections.list(getParameterNames());

		for (String key : lst_param_names)
		{
			map.put(key, getParameterValues(key));
		}

		return map;
	}

	@Override
	public Enumeration<String> getParameterNames()
	{
		Map<String, List<String>> netty_params = netty_request.getParameters();
		Set<String> netty_param_names = netty_params.keySet();
		return Collections.enumeration(netty_param_names);
	}

	@Override
	public String[] getParameterValues(String name)
	{
		List<String> l_values = netty_request.getParameters(name);
		return l_values.toArray(new String[l_values.size()]);
	}

	@Override
	public String getProtocol()
	{
		return netty_request.getProtocolVersion().toString();
	}

	@Override
	public BufferedReader getReader() throws IOException
	{
		ChannelBuffer buf = netty_request.getContent();
		final ChannelBufferInputStream ch_id = new ChannelBufferInputStream(buf);
		return new BufferedReader(new InputStreamReader(ch_id));
	}

	@Override
	public String getRealPath(String path)
	{
		throw new UnsupportedOperationException("getRealPath is not implemented");
	}

	@Override
	public String getRemoteAddr()
	{
		return netty_ctx.getChannel().getRemoteAddress().toString();
	}

	@Override
	public String getRemoteHost()
	{
		return netty_ctx.getChannel().getRemoteAddress().toString();
	}

	@Override
	public int getRemotePort()
	{
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path)
	{
		throw new UnsupportedOperationException("getRequestDispatcher is not implemented");
	}

	@Override
	public String getScheme()
	{
		return "http";
	}

	@Override
	public String getServerName()
	{
		return "Netty Servlet engine";
	}

	@Override
	public int getServerPort()
	{
		return 0;
	}

	@Override
	public boolean isSecure()
	{
		return false;
	}

	@Override
	public void removeAttribute(String name)
	{
		request_attributes.remove(name);
	}

	@Override
	public void setAttribute(String name, Object value)
	{
		request_attributes.put(name, value);
	}

	@Override
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException
	{
		netty_request.setCharacterEncoding(env);
	}

	@Override
	public String getAuthType()
	{
		return null;
	}

	@Override
	public String getContextPath()
	{
		return netty_request.getUri();
	}

	@Override
	public Cookie[] getCookies()
	{
		return new Cookie[0];
	}

	@Override
	public long getDateHeader(String name)
	{
		try
		{
			String value = getParameter(name);
			long epoch = Long.parseLong(value);
			return epoch;
		}
		catch (Throwable t)
		{
			throw new IllegalArgumentException("Header value can't be converted to a date");
		}
	}

	@Override
	public String getHeader(String name)
	{
		return netty_request.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaderNames()
	{
		Set<String> lst_header_name = netty_request.getHeaderNames();
		return Collections.enumeration(lst_header_name);
	}

	@Override
	public Enumeration<String> getHeaders(String name)
	{
		List<String> lst_headers = netty_request.getHeaders(name);
		return Collections.enumeration(lst_headers);
	}

	@Override
	public int getIntHeader(String name)
	{
		String value = getParameter(name);
		return Integer.parseInt(value);
	}

	@Override
	public String getMethod()
	{
		return netty_request.getMethod().getName();
	}

	@Override
	public String getPathInfo()
	{
		return null;
	}

	@Override
	public String getPathTranslated()
	{
		return null;
	}

	@Override
	public String getQueryString()
	{
		return netty_request.getQueryString();
	}

	@Override
	public String getRemoteUser()
	{
		return null;
	}

	@Override
	public String getRequestURI()
	{
		return netty_request.getUri();
	}

	@Override
	public StringBuffer getRequestURL()
	{
		throw new UnsupportedOperationException("getRequestURL is not implemented");
	}

	@Override
	public String getRequestedSessionId()
	{
		return null;
	}

	@Override
	public String getServletPath()
	{
		throw new UnsupportedOperationException("getServletPath is not implemented");
	}

	@Override
	public HttpSession getSession()
	{
		return null;
	}

	@Override
	public HttpSession getSession(boolean create)
	{
		if (create)
		{
			throw new UnsupportedOperationException("getSession(true) is not implemented");
		}
		else
		{
			return null;
		}
	}

	@Override
	public Principal getUserPrincipal()
	{
		throw new UnsupportedOperationException("getUserPrincipal is not implemented");
	}

	@Override
	public boolean isRequestedSessionIdFromCookie()
	{
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL()
	{
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl()
	{
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid()
	{
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0)
	{
		return false;
	}
}