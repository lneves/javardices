package pt.sapo.sandbox.examples;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.caudexorigo.jersey.netty.http.NettyHttpServer;
import org.caudexorigo.jersey.netty.http.NettyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainJerseyNetty4HttpServer
{
	private static Logger log = LoggerFactory.getLogger(MainJerseyNetty4HttpServer.class);

	private static final String RESOURCES_PACKAGE = "pt.sapo.sandbox.examples";
	private static final int PORT = 8383;
	private static final String HOSTNAME = "0.0.0.0";
	private static NettyHttpServer server;

	public static void main(String[] s) throws URISyntaxException, InterruptedException, IOException
	{
		InetSocketAddress inet = new InetSocketAddress("0.0.0.0", 8383);

		final String baseUri = String.format("http://%s:%s/", HOSTNAME, PORT);

		ResourceConfig rc = getResourceConfiguration(baseUri);

		if (log.isDebugEnabled())
		{
			log.debug(String.format("Resources: %s", rc.getClasses()));
		}

		server = NettyHttpServerFactory.create(rc, inet);
		server.startServer();
		log.info(String.format("Server listening %s", baseUri));
		Scanner in = new Scanner(System.in);
		System.out.flush();
		String str = "";
		while (!str.equals("0"))
		{
			in.reset();
			str = in.next();
			if (str.equals("r"))
				server.reload();
		}
		server.stopServer();
	}

	private static ResourceConfig getResourceConfiguration(final String baseUri)
	{
		final ResourceConfig rc = new ResourceConfig();
		rc.packages(RESOURCES_PACKAGE);
		return rc;
	}
}