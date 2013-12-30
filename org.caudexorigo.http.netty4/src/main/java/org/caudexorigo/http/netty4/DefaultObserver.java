package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class DefaultObserver implements RequestObserver
{
	@Override
	public void begin(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		// do nothing
	}

	@Override
	public void end(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		// do nothing
	}
}