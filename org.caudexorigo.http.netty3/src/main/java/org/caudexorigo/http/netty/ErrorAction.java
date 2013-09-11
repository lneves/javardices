package org.caudexorigo.http.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class ErrorAction extends HttpAction
{
	private final WebException ex;

	public ErrorAction(WebException ex)
	{
		super();
		this.ex = ex;
	}

	@Override
	public void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		throw ex;

	}
}