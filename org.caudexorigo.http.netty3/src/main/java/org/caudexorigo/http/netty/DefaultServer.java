package org.caudexorigo.http.netty;

import org.caudexorigo.cli.CliFactory;
import org.caudexorigo.http.netty.reporting.StandardResponseFormatter;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

public class DefaultServer
{
	public static void main(String[] args) throws Exception
	{
		final NettyHttpServerCliArgs cargs = CliFactory.parseArguments(NettyHttpServerCliArgs.class, args);

		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		NettyHttpServer server = new NettyHttpServer(true);
		server.setPort(cargs.getPort());
		server.setHost(cargs.getHost());
		server.setRouter(new DefaultRouter());
		server.setResponseFormtter(new StandardResponseFormatter(false));
		server.start();
	}
}