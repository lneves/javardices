package pt.sapo.sandbox.examples;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class MainJerseyGizzlyHttpServer
{
	private static Logger log = LoggerFactory.getLogger(MainJerseyGizzlyHttpServer.class);
	private static final String BASE_URI = "http://0.0.0.0:8181/";

	public static void main(String[] args)
	{
		try
		{
			// Rremove existing handlers attached to j.u.l root logger
			SLF4JBridgeHandler.removeHandlersForRootLogger(); // (since SLF4J 1.6.5)

			// add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
			// the initialization phase of your application
			SLF4JBridgeHandler.install();

			final ResourceConfig resourceConfig = new ResourceConfig();
			resourceConfig.packages("pt.sapo.sandbox.examples");

			HttpServer server = GrizzlyHttpServerFactory.createHttpServer(new URI(BASE_URI), resourceConfig);
			log.info("Press Enter to stop the server. ");
			System.out.flush();
			System.in.read();
			server.stop();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
}