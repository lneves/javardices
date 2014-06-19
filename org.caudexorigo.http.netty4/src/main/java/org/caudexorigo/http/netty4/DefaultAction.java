package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;

public class DefaultAction extends HttpAction
{
	public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		super.getResponseFormatter().formatResponse(request, response);
	}
}