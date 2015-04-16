package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

import org.caudexorigo.http.netty4.HttpAction;

public class CorsAction extends HttpAction
{
	@Override
	public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		response.headers().add(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		response.headers().add(HttpHeaders.Names.ALLOW, "GET,HEAD,POST,OPTIONS");
		response.setStatus(HttpResponseStatus.OK);
	}
}