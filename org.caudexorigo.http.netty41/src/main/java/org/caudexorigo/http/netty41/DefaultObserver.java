package org.caudexorigo.http.netty41;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class DefaultObserver implements RequestObserver
{
	@Override
	public void begin(ChannelHandlerContext ctx, HttpRequest request)
	{
		// do nothing
	}

	@Override
	public void end(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		// do nothing
	}
}