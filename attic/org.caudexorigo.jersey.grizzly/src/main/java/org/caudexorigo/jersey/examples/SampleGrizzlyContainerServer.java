package org.caudexorigo.jersey.examples;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

//import org.slf4j.bridge.SLF4JBridgeHandler;

public class SampleGrizzlyContainerServer
{
	private static final String BASE_URI = "http://0.0.0.0:8082/";

	public static void main(String[] args) throws Exception
	{
		// // Rremove existing handlers attached to j.u.l root logger
		// SLF4JBridgeHandler.removeHandlersForRootLogger(); // (since SLF4J 1.6.5)
		//
		// // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
		// // the initialization phase of your application
		// SLF4JBridgeHandler.install();

		final ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.packages("org.caudexorigo.jersey.examples");

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(new URI(BASE_URI), resourceConfig);
		//log.info("Press Enter to stop the server. ");
		System.out.flush();
		System.in.read();
		server.stop();
	}
}