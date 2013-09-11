package org.caudexorigo.jersey.netty.http;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.spi.ContainerProvider;

public final class NettyContainerProvider implements ContainerProvider
{
	@Override
	public <T> T createContainer(final Class<T> type, final ApplicationHandler application) throws ContainerException
	{
		NettyHttpHandler handler = null;
		if (type == NettyHttpHandler.class)
		{
			handler = new NettyHttpHandler(application, application.getConfiguration());
		}
		return type.cast(handler);
	}
}