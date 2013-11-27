package org.caudexorigo.http.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class DefaultObserver implements RequestObserver
{
	@Override
	public void begin(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		// do nothing
	}

	@Override
	public void end(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		// do nothing
	}
}