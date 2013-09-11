package org.caudexorigo.http.netty;

import org.caudexorigo.http.netty.HttpAction;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class RedirectAction extends HttpAction
{
	private final String path;

	public RedirectAction(String path)
	{
		super();
		this.path = path;
	}

	@Override
	public void service(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res)
	{
		res.setStatus(HttpResponseStatus.FOUND);
		res.addHeader(HttpHeaders.Names.LOCATION, path);
	}
}