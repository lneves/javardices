package org.caudexorigo.http.netty4;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import org.caudexorigo.Shutdown;
import org.caudexorigo.cli.CliFactory;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;

public class DefaultServer
{
	public static void main(String[] args)
	{
		try
		{
			final NettyHttpServerCliArgs cargs = CliFactory.parseArguments(NettyHttpServerCliArgs.class, args);

			InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
			NettyHttpServer server = new NettyHttpServer(cargs.getHost(), cargs.getPort(), false);
			server.setRouter(new DefaultRouter());
			server.setResponseFormtter(new StandardResponseFormatter(false));
			server.start();
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}
}