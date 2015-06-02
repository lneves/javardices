package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class HelloWorldAction extends HttpAction
{
	// private byte[] hello = "Hello World".getBytes();

	public HelloWorldAction()
	{
		super();
	}

	@Override
	public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		byte[] hello = String.format("Hello World%nHello World%nHello World%nHello World%nHello World%nHello World%nHello World%nHello World%nHello World%nHello World%nHello World%nHello World%nHello World -> %s%n", System.currentTimeMillis()).getBytes();
		response.content().writeBytes(hello);
		throw new RuntimeException("break all the things!");
	}
}