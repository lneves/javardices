package org.caudexorigo.http.netty;

import org.jboss.netty.handler.codec.http.HttpRequest;

public interface RequestRouter
{
	public HttpAction map(HttpRequest req);
}