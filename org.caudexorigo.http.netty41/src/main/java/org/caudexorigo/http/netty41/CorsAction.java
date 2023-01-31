package org.caudexorigo.http.netty41;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;

public class CorsAction extends HttpAction
{
	@Override
	public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		response.headers().add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		response.headers().add(HttpHeaderNames.ALLOW, "GET,HEAD,POST,OPTIONS");
		response.headers().add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "x-request-with, x-requested-with, content-type");
		response.setStatus(HttpResponseStatus.OK);

	}
}