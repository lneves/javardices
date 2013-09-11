package org.caudexorigo.jersey.examples;

import org.caudexorigo.cli.CliFactory;
import org.caudexorigo.http.netty.CliArgs;
import org.caudexorigo.http.netty.NettyHttpServer;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

public class SampleNettyContainerServer
{
	public static void main(String[] args) throws Exception
	{
		final CliArgs cargs = CliFactory.parseArguments(CliArgs.class, args);

		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		NettyHttpServer server = new NettyHttpServer();
		server.setPort(cargs.getPort());
		server.setHost(cargs.getHost());
		server.setRouter(new SampleJerseyRouter());
		server.start();
	}
}