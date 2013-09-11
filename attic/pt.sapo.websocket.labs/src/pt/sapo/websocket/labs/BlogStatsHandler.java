package pt.sapo.websocket.labs;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.concurrent.CustomExecutors;
import org.caudexorigo.http.netty.WebSocketHandler;
import org.caudexorigo.text.DateUtil;
import org.caudexorigo.text.StringEscapeUtils;
import org.caudexorigo.text.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.websocket.DefaultWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocket.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.client.messaging.BrokerListener;
import pt.com.broker.types.NetNotification;

public class BlogStatsHandler implements BrokerListener, WebSocketHandler
{
	private static Logger log = LoggerFactory.getLogger(TwitterHandler.class);

	private static final ScheduledExecutorService sched_exec = CustomExecutors.newScheduledThreadPool(10, "BlogStatsHandlerSchedExec");

	private final Set<Channel> _members = Collections.newSetFromMap(new ConcurrentHashMap<Channel, Boolean>());

	private String last_global_status_message = "";
	private String last_top_hosts_message = "";
	private String last_top_pages_message = "";
	private String last_blog_post_message = "";
	private String last_sources_message = "";

	public BlogStatsHandler()
	{
		super();

		Runnable db_janitor = new Runnable()
		{
			public void run()
			{
				try
				{
					BlogDbHelper.deleteStale();
					BlogDbHelper.trackVisits();
				}
				catch (Throwable e)
				{
					Throwable r = ErrorAnalyser.findRootCause(e);
					log.error(r.getMessage(), r);
				}

			}
		};

		sched_exec.scheduleWithFixedDelay(db_janitor, 0, 1, TimeUnit.MINUTES);

		Runnable global_status_broadcaster = new Runnable()
		{
			public void run()
			{
				try
				{
					last_global_status_message = BlogDbHelper.getGlobalStatus();
					broadcastMessage(last_global_status_message);

				}
				catch (Throwable e)
				{
					Throwable r = ErrorAnalyser.findRootCause(e);
					log.error(r.getMessage(), r);
				}
			}
		};

		sched_exec.scheduleWithFixedDelay(global_status_broadcaster, 0, 10, TimeUnit.SECONDS);

		Runnable top_hosts_broadcaster = new Runnable()
		{
			public void run()
			{
				try
				{
					last_top_hosts_message = BlogDbHelper.getTopHosts();
					last_top_pages_message = BlogDbHelper.getTopPages();
					last_sources_message = BlogDbHelper.getSources();
					broadcastMessage(last_top_hosts_message);
					broadcastMessage(last_top_pages_message);
					broadcastMessage(last_sources_message);
				}
				catch (Throwable e)
				{
					Throwable r = ErrorAnalyser.findRootCause(e);
					log.error(r.getMessage(), r);
				}
			}
		};

		sched_exec.scheduleWithFixedDelay(top_hosts_broadcaster, 0, 30, TimeUnit.SECONDS);

	}

	@Override
	public void handleMessage(Channel channel, WebSocketFrame ws_frame)
	{
		if (ws_frame.isText())
		{
			handleTextMessage(channel, ws_frame.getTextData());
		}
		else if (ws_frame.isBinary())
		{
			handleBinaryMessage(channel, ws_frame.getBinaryData());
		}
		else
		{
			throw new IllegalArgumentException("Unknown message type for 'pt.sapo.websocket.labs.BlogStatsHandler'");
		}
	}

	public void handleTextMessage(Channel channel, String message)
	{

	}

	public void handleBinaryMessage(Channel channel, ChannelBuffer buffer)
	{
		throw new IllegalArgumentException("Processing of binary messages is not implemented in the 'pt.sapo.websocket.labs.BlogStatsHandler' handler");
	}

	@Override
	public void handleWebSocketClosed(Channel channel)
	{
		boolean closed = _members.remove(channel);
		if (closed)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Web Socket Closed: '{}'", channel.getRemoteAddress());
			}
		}
	}

	@Override
	public void handleWebSocketOpened(final Channel channel)
	{
		_members.add(channel);
		if (log.isDebugEnabled())
		{
			log.debug("Web Socket Open: '{}'", channel.getRemoteAddress());
		}

		Runnable init = new Runnable()
		{
			public void run()
			{
				sendMessage(channel, last_global_status_message);
				sendMessage(channel, last_top_hosts_message);
				sendMessage(channel, last_top_pages_message);
				sendMessage(channel, last_blog_post_message);
			}
		};

		sched_exec.schedule(init, 1, TimeUnit.SECONDS);
	}

	@Override
	public boolean isAutoAck()
	{
		return false;
	}

	@Override
	public void onMessage(NetNotification nnot)
	{
		if ("/sapo/webanalytics/page-view/.*blogs.sapo.pt".equals(nnot.getSubscription()))
		{
			handlePageView(nnot);
		}
		else if ("/gnosis/documents/blogs/new/.*.blogs.sapo.pt".equals(nnot.getSubscription()))
		{
			handlePost(nnot);
		}
	}

	private void handlePost(NetNotification nnot)
	{
		byte[] raw_message = nnot.getMessage().getPayload();

		String m = new String(raw_message);
		String url = StringEscapeUtils.unescapeXml(StringUtils.substringBetween(m, "<url>", "</url>"));
		
		String title = StringEscapeUtils.unescapeXml(StringUtils.substringBetween(m, "<title>", "</title>"));
		String author = StringEscapeUtils.unescapeXml(StringUtils.substringBetween(m, "<author>", "</author>"));
		Date pubdate = DateUtil.parseISODate((StringUtils.substringBetween(m, "<pubdate>", "</pubdate>")));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();

		((ObjectNode) rootNode).put("type", "new_blog_post");
		((ObjectNode) rootNode).put("url", url);
		((ObjectNode) rootNode).put("title", title);
		((ObjectNode) rootNode).put("author", author);
		((ObjectNode) rootNode).put("pubdate_ts", pubdate.getTime());

		String new_post_message = rootNode.toString();
		last_blog_post_message = new_post_message;

		broadcastMessage(new_post_message);

	}

	private void handlePageView(NetNotification nnot)
	{
		byte[] raw_message = nnot.getMessage().getPayload();

		String m = new String(raw_message);

		String hostname = StringUtils.substringBetween(m, "<host>", "</host>");
		String url = StringEscapeUtils.unescapeXml(StringUtils.substringBetween(m, "<url>", "</url>"));
		
		url = StringUtils.substringBefore(url, "?");
		url = StringUtils.substringBefore(url, "#");
		
		String doc_title = StringEscapeUtils.unescapeXml(StringUtils.substringBetween(m, "<doc-title>", "</doc-title>"));
		boolean new_site_visit = Boolean.parseBoolean(StringUtils.substringBetween(m, "<new-site-visit>", "</new-site-visit>"));
		String referer = StringUtils.trimToNull(StringUtils.substringBetween(m, "<referer>", "</referer>"));
		String referer_host = null;

		if (StringUtils.isNotBlank(referer))
		{
			referer_host = StringUtils.substringBefore(StringUtils.substringAfter(referer, "http://"), "/");
		}
		boolean is_direct = ((referer_host == null) || StringUtils.contains(referer_host, "blogs.sapo.pt"));

		String keywords = StringUtils.substringBetween(m, "<keywords>", "</keywords>");
		long site_visit_id = Long.parseLong(StringUtils.substringBetween(m, "<site-visit-id>", "</site-visit-id>"));

		String municipality_id = StringUtils.trimToNull(StringUtils.substringBetween(m, "municipality id=\"", "\">"));

		BlogDbHelper.insertPageView(site_visit_id, new_site_visit, hostname, url, doc_title, is_direct, referer_host, keywords, municipality_id);
	}

	private void broadcastMessage(String broadcast_message)
	{
		if (StringUtils.isNotBlank(broadcast_message))
		{
			DefaultWebSocketFrame ws_frame = new DefaultWebSocketFrame(broadcast_message);

			int mark = ws_frame.getBinaryData().readerIndex();

			log.debug("Broadcast message: '{}'", broadcast_message);

			for (Channel ch : _members)
			{
				try
				{
					ch.write(ws_frame);
					ws_frame.getBinaryData().readerIndex(mark);
				}
				catch (Throwable t)
				{
					Throwable r = ErrorAnalyser.findRootCause(t);
					log.error(r.getMessage());
				}
			}
		}
		else
		{
			log.debug("No message to broadcast");
		}
	}
	
	private void sendMessage(Channel channel , String broadcast_message)
	{
		if (StringUtils.isNotBlank(broadcast_message))
		{
			DefaultWebSocketFrame ws_frame = new DefaultWebSocketFrame(broadcast_message);

			int mark = ws_frame.getBinaryData().readerIndex();

			log.debug("Send message: '{}'", broadcast_message);

			try
			{
				channel.write(ws_frame);
				ws_frame.getBinaryData().readerIndex(mark);
			}
			catch (Throwable t)
			{
				Throwable r = ErrorAnalyser.findRootCause(t);
				log.error(r.getMessage());
			}
		}
		else
		{
			log.debug("No message to send");
		}
	}
}