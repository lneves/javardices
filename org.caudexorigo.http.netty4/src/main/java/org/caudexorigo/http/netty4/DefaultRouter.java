package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class DefaultRouter implements RequestRouter
{
	private final HttpAction hello = new HelloWorldAction();

	@Override
	public HttpAction map(ChannelHandlerContext ctx, FullHttpRequest req)
	{
		if ("/hello".equals(req.getUri()))
		{
			return hello;
		}
		return null;
	}
}