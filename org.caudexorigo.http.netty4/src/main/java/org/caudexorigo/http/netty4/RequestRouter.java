package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface RequestRouter
{
	public HttpAction map(ChannelHandlerContext ctx, FullHttpRequest request);
}