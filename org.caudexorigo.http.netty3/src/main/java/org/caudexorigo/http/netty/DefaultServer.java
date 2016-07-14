package org.caudexorigo.http.netty;

import org.caudexorigo.Shutdown;
import org.caudexorigo.http.netty.reporting.StandardResponseFormatter;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

import com.lexicalscope.jewel.cli.CliFactory;

public class DefaultServer
{
	public static void main(String[] args)
	{
		try
		{
			final NettyHttpServerCliArgs cargs = CliFactory.parseArguments(NettyHttpServerCliArgs.class, args);

			InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
			NettyHttpServer server = new NettyHttpServer("0.0.0.0", cargs.getPort(), true);
			server.setPort(cargs.getPort());
			server.setHost(cargs.getHost());
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