package org.caudexorigo.http.netty;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWebSocketHandler implements WebSocketHandler
{
	private static final Logger log = LoggerFactory.getLogger(DefaultWebSocketHandler.class);

	private static final Set<Channel> _members = Collections.newSetFromMap(new ConcurrentHashMap<Channel, Boolean>());

	public DefaultWebSocketHandler()
	{
		super();
	}

	@Override
	public void handleMessage(Channel channel, WebSocketFrame ws_frame)
	{
		if (ws_frame.i)
		{
			handleTextMessage(channel, ws_frame.getTextData());
		}
		else if (ws_frame.isBinary())
		{
			handleBinaryMessage(channel, ws_frame.getBinaryData());
		}
		else
		{
			throw new IllegalArgumentException("Unknown message type for 'org.caudexorigo.http.netty.DefaultWebSocketHandler'");
		}
	}

	public void handleTextMessage(Channel channel, String message)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Channel '{}'. Message: '{}'", channel.getRemoteAddress(), message);
		}

		if (log.isDebugEnabled())
		{
			int s = _members.size();
			if (s == 0)
			{
				log.debug("No connected clients");
			}
			if (s == 1)
			{
				log.debug("Send message to 1 connected client");
			}
			else
			{
				log.debug("Send message to {} connected clients", s);
			}
		}
		DefaultWebSocketFrame ws_frame = new DefaultWebSocketFrame(message);

		int mark = ws_frame.getBinaryData().readerIndex();

		for (Channel ch : _members)
		{
			// DefaultWebSocketFrame ws_frame = new DefaultWebSocketFrame(message.toUpperCase());
			ch.write(ws_frame);
			ws_frame.getBinaryData().readerIndex(mark);
		}
	}

	public void handleBinaryMessage(Channel channel, ChannelBuffer buffer)
	{
		throw new IllegalArgumentException("Processing of binary messages is not implemented in the Default 'web sockets' handler");
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
	public void handleWebSocketOpened(Channel channel)
	{
		_members.add(channel);
		if (log.isDebugEnabled())
		{
			log.debug("Web Socket Open: '{}'", channel.getRemoteAddress());
		}
	}
}