package org.caudexorigo.http.netty;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface WebSocketHandler
{
	public abstract void handleMessage(Channel channel, WebSocketFrame ws_frame);

	public abstract void handleWebSocketClosed(Channel channel);

	public abstract void handleWebSocketOpened(Channel channel);

}