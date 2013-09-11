package org.caudexorigo.jersey.netty.http;

import java.net.SocketAddress;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;

public final class NettyHttpServerFactory
{
	private NettyHttpServerFactory()
	{
	}

	public static NettyHttpServer create(final ResourceConfig resourceConfig, final SocketAddress inet)
	{
		ApplicationHandler application = new ApplicationHandler(resourceConfig);
		// final NettyHttpHandler jerseyHandler = ContainerFactory.createContainer(NettyHttpHandler.class, resourceConfig);
		return new NettyHttpServer(new NettyHttpServerChannelInitializer(application, resourceConfig), inet);
	}
}