package org.caudexorigo.jpt.sample;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.io.File;
import java.net.URI;

import org.caudexorigo.Shutdown;
import org.caudexorigo.cli.CliFactory;
import org.caudexorigo.http.netty4.NettyHttpServer;
import org.caudexorigo.http.netty4.NettyHttpServerCliArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
	private static Logger log = LoggerFactory.getLogger(Main.class);

	static
	{
		ResourceLeakDetector.setLevel(Level.DISABLED);
	}

	public static void main(String[] args) throws Throwable
	{
		try
		{
			NettyHttpServerCliArgs cargs = CliFactory.parseArguments(NettyHttpServerCliArgs.class, args);

			String root_dir = cargs.getRootDirectory();

			File r = new File(root_dir);

			URI root_uri = r.getCanonicalFile().toURI();

			String host = cargs.getHost();
			int port = cargs.getPort();

			log.info(String.format("Server Init: %nRoot Directory: %s%nHost: %s%nPort: %s%n", root_uri.toASCIIString(), host, port));

			InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
			NettyHttpServer server = new NettyHttpServer();
			server.setValidateHeaders(false);
			server.setHost(host);
			server.setPort(port);
			server.setRouter(new AppMapper(root_uri));

			server.start();

			log.info("Server Started");
		}
		catch (Throwable e)
		{
			Shutdown.now(e);
		}
	}
}