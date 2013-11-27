package wstest.srv.netty;

import org.caudexorigo.cli.CliFactory;
import org.caudexorigo.http.netty.CliArgs;
import org.caudexorigo.http.netty.NettyHttpServer;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyWSTestSrv
{
	private static Logger log = LoggerFactory.getLogger(NettyWSTestSrv.class);

	private static final int PORT = 8012;

	public static void main(String[] args) throws Exception
	{
		log.info("Starting Httpd");

		try
		{
			// Verify if the Aalto parser is in the classpath
			Class.forName("com.fasterxml.aalto.stax.InputFactoryImpl").newInstance();
			Class.forName("com.fasterxml.aalto.stax.OutputFactoryImpl").newInstance();
			Class.forName("com.fasterxml.aalto.stax.EventFactoryImpl").newInstance();

			// If we made it here without errors set Aalto as our StaX parser
			System.setProperty("javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
			System.setProperty("javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
			System.setProperty("javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
		}
		catch (Throwable t)
		{
			log.warn("Aalto was not found in the classpath, will fallback to use the native parser");
		}

		final CliArgs cargs = CliFactory.parseArguments(CliArgs.class, args);

		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		NettyHttpServer server = new NettyHttpServer();
		server.setPort(cargs.getPort());
		server.setHost(cargs.getHost());
		server.setRouter(new NettyWSRouter());
		server.start();
	}
}
