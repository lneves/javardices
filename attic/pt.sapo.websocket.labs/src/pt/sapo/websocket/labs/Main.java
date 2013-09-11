package pt.sapo.websocket.labs;

import org.caudexorigo.cli.CliFactory;
import org.caudexorigo.http.netty.CliArgs;
import org.caudexorigo.http.netty.DefaultWebSocketHandler;
import org.caudexorigo.http.netty.NettyHttpServer;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.client.BrokerClient;
import pt.com.broker.types.NetSubscribe;
import pt.com.broker.types.NetAction.DestinationType;

public class Main
{
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Throwable
	{
		final CliArgs cargs = CliFactory.parseArguments(CliArgs.class, args);

		TwitterHandler twitter_handler = new TwitterHandler();
		BlogStatsHandler blog_stats_handler = new BlogStatsHandler();

		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		String dir = cargs.getRootDirectory();
		NettyHttpServer server = new NettyHttpServer(dir);
		server.setRouter(new LabsRouter());
		server.setPort(cargs.getPort());
		server.setHost(cargs.getHost());
		server.addWebSocketHandler("/websocket/twitter", twitter_handler);
		server.addWebSocketHandler("/websocket/sample", new DefaultWebSocketHandler());
		//server.addWebSocketHandler("/websocket/blogstats", blog_stats_handler);
		server.start();

		BrokerClient bk0 = new BrokerClient("broker.labs.sapo.pt", 3323);
		log.info("Broker ping, got actionId: '{}'", bk0.checkStatus().getActionId());
		bk0.addAsyncConsumer(new NetSubscribe("/social/portugal/twitter", DestinationType.TOPIC), twitter_handler);
		log.info("Subscribed to '/social/portugal/twitter'");

		BrokerClient bk1 = new BrokerClient("10.135.6.12", 3323);
		log.info("Broker ping, got actionId: '{}'", bk1.checkStatus().getActionId());
		bk1.addAsyncConsumer(new NetSubscribe("/sapo/webanalytics/page-view/.*blogs.sapo.pt", DestinationType.TOPIC), blog_stats_handler);
		bk1.addAsyncConsumer(new NetSubscribe("/gnosis/documents/blogs/new/.*.blogs.sapo.pt", DestinationType.TOPIC), blog_stats_handler);

		log.info("Subscribed to '/sapo/webanalytics/page-view/.*blogs.sapo.pt'");

	}
}
