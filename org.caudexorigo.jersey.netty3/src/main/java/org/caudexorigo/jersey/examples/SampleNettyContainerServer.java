package org.caudexorigo.jersey.examples;


import org.caudexorigo.http.netty.NettyHttpServer;
import org.caudexorigo.http.netty.NettyHttpServerCliArgs;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

import com.lexicalscope.jewel.cli.CliFactory;

public class SampleNettyContainerServer
{
	public static void main(String[] args) throws Exception
	{
		final NettyHttpServerCliArgs cargs = CliFactory.parseArguments(NettyHttpServerCliArgs.class, args);

		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		NettyHttpServer server = new NettyHttpServer();
		server.setPort(cargs.getPort());
		server.setHost(cargs.getHost());
		server.setRouter(new SampleJerseyRouter());
		server.start();
	}
}