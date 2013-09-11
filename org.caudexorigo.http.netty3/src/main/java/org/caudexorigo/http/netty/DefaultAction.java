package org.caudexorigo.http.netty;

import org.caudexorigo.http.netty.reporting.StandardResponseFormatter;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class DefaultAction extends HttpAction
{
	private StandardResponseFormatter _stdFrm = new StandardResponseFormatter(false);

	public void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		_stdFrm.formatResponse(request, response);
	}
}