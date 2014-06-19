package org.caudexorigo.http.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class DefaultAction extends HttpAction
{
	public void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		super.getResponseFormatter().formatResponse(request, response);
	}
}