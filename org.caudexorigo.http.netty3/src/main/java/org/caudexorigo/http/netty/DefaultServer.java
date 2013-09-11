package org.caudexorigo.http.netty;

import java.net.URI;

import org.caudexorigo.cli.CliFactory;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

public class DefaultServer
{
	public static void main(String[] args) throws Exception
	{
		final CliArgs cargs = CliFactory.parseArguments(CliArgs.class, args);

		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		String dir = cargs.getRootDirectory();
		URI root_dir = URI.create(dir);
		NettyHttpServer server = new NettyHttpServer(root_dir, true);
		server.setPort(cargs.getPort());
		server.setHost(cargs.getHost());
		server.setRouter(new DefaultRouter());
		server.start();
	}
}