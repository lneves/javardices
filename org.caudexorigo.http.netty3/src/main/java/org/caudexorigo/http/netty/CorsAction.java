package org.caudexorigo.http.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class CorsAction extends HttpAction
{
	@Override
	public void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		response.headers().add(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		response.headers().add(HttpHeaders.Names.ALLOW, "GET,HEAD,POST,OPTIONS");
		response.headers().add("Access-Control-Allow-Headers", "X-Requested-With");
		response.setStatus(HttpResponseStatus.OK);
	}
}