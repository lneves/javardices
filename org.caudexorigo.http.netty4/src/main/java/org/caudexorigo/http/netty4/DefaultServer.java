package org.caudexorigo.http.netty4;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.net.URI;

import org.caudexorigo.cli.CliFactory;

public class DefaultServer
{
	public static void main(String[] args) throws Exception
	{
		final CliArgs cargs = CliFactory.parseArguments(CliArgs.class, args);

		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		String dir = cargs.getRootDirectory();

		System.out.println("DefaultServer.main.root_dir: " + dir);
		URI root_dir = URI.create("file://" + dir);
		System.out.println("DefaultServer.main.root_uri: " + root_dir.toString());

		NettyHttpServer server = new NettyHttpServer(root_dir);
		server.setPort(cargs.getPort());
		server.setHost(cargs.getHost());
		server.setRouter(new DefaultRouter());
		server.start();
	}
}