package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface RequestObserver
{
	public void begin(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response);

	public void end(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response);
}