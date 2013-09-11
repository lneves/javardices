package org.caudexorigo.http;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.HttpRequestWrapper;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class NettyServletAdapter extends HttpAction
{
	private final HttpServlet servlet;

	public NettyServletAdapter(HttpServlet servlet, String servletName, String xpto)
	{
		super();
		this.servlet = servlet;
	}

	@Override
	public void service(ChannelHandlerContext ctx, HttpRequest http_request, HttpResponse http_response)
	{
		try
		{
			HttpServletRequest servlet_request = new NettyServletRequestWrapper(this, ctx, (HttpRequestWrapper) http_request);
			HttpServletResponse servlet_response = new NettyServletResponseWrapper(this, ctx, http_response);

			servlet.service(servlet_request, servlet_response);
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}
}