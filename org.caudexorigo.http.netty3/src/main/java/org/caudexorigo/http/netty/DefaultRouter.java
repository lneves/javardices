package org.caudexorigo.http.netty;

import org.jboss.netty.handler.codec.http.HttpRequest;

public class DefaultRouter implements RequestRouter
{
	private final HttpAction hello = new HelloWorldAction();
	private final HttpAction defaultAction = new DefaultAction();

	@Override
	public HttpAction map(HttpRequest req)
	{
		if ("/hello".equals(req.getUri()))
		{
			return hello;
		}
		return defaultAction;
	}
}