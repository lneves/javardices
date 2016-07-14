package org.caudexorigo.http.netty4;

import org.caudexorigo.Shutdown;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

public class DefaultServer
{
	public static void main(String[] args)
	{
		try
		{
			String host = "0.0.0.0";
			int port = 8000;

			InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.getDefaultFactory());
			NettyHttpServer server = new NettyHttpServer(host, port);
			server.setRouter(new DefaultRouter());
			server.setResponseFormatter(new StandardResponseFormatter(true));
			System.out.printf("listening on: %s%n", port);
			server.start();
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}
}