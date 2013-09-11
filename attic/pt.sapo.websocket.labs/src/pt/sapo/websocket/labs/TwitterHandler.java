package pt.sapo.websocket.labs;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.concurrent.CustomExecutors;
import org.caudexorigo.http.netty.WebSocketHandler;
import org.caudexorigo.text.StringUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
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

public class TwitterHandler implements BrokerListener, WebSocketHandler
{
	private static final ScheduledExecutorService sched_exec = CustomExecutors.newScheduledThreadPool(1, "TwitterHandlerSchedExec");

	private static Logger log = LoggerFactory.getLogger(TwitterHandler.class);

	private final Set<Channel> _members = Collections.newSetFromMap(new ConcurrentHashMap<Channel, Boolean>());

	private JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());

	private String previous_text = "";
	private String previous_message = "";

	public TwitterHandler()
	{
		super();
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
			throw new IllegalArgumentException("Unknown message type for 'pt.sapo.websocket.labs.TwitterHandler'");
		}
	}

	public void handleTextMessage(Channel channel, String message)
	{

	}

	public void handleBinaryMessage(Channel channel, ChannelBuffer buffer)
	{
		throw new IllegalArgumentException("Processing of binary messages is not implemented in the 'pt.sapo.websocket.labs.TwitterHandler' handler");
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
				sendMessage(channel, previous_message);
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
		if (_members.size() == 0)
		{
			log.debug("No connected clients, skip processing");
			return;
		}

		byte[] raw_message = nnot.getMessage().getPayload();

		if (log.isDebugEnabled())
		{
			String rec_message = new String(raw_message);
			log.debug("Got : '{}'", rec_message);
		}

		String broadcast_message = "";

		try
		{
			JsonParser jp = jsonFactory.createJsonParser(raw_message);
			JsonNode json_msg = jp.readValueAsTree();

			long id = json_msg.get("id").getLongValue();
			String text = json_msg.get("text").getTextValue();

			if (previous_text.equals(text))
			{
				return;
			}
			else
			{
				previous_text = text;
			}
			// String created_at = json_msg.get("created_at").getTextValue();
			JsonNode user = json_msg.get("user");
			String profile_image_url = user.get("profile_image_url").getTextValue();
			String screen_name = user.get("screen_name").getTextValue();

			if (StringUtils.isNotBlank(screen_name))
			{
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.createObjectNode();

				((ObjectNode) rootNode).put("id", id);
				((ObjectNode) rootNode).put("from_user", screen_name);
				((ObjectNode) rootNode).put("text", text);
				// ((ObjectNode) rootNode).put("created_at", created_at);
				((ObjectNode) rootNode).put("profile_image_url", profile_image_url);

				broadcast_message = rootNode.toString();
			}
		}
		catch (Throwable t)
		{
			if (log.isDebugEnabled())
			{
				Throwable r = ErrorAnalyser.findRootCause(t);
				log.error(r.getMessage(), r);
			}
		}

		broadcastMessage(broadcast_message);
	}

	private void broadcastMessage(String broadcast_message)
	{
		if (StringUtils.isNotBlank(broadcast_message))
		{
			DefaultWebSocketFrame ws_frame = new DefaultWebSocketFrame(broadcast_message);

			int mark = ws_frame.getBinaryData().readerIndex();

			log.debug("Broadcast message: '{}'", broadcast_message);

			previous_message = broadcast_message;

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

	private void sendMessage(Channel channel, String broadcast_message)
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