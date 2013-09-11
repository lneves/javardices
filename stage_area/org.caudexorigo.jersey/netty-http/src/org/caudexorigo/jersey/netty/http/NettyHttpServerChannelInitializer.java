package org.caudexorigo.jersey.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NettyHttpServerChannelInitializer extends ChannelInitializer<SocketChannel>
{
	private static Logger log = LoggerFactory.getLogger(NettyHttpServerChannelInitializer.class);
	
	private final NettyHttpHandler jersey_handler;

	public NettyHttpServerChannelInitializer(ApplicationHandler application, ResourceConfig resourceConfig)
	{
		log.debug(String.format("new (%s, %s)", application.toString(), resourceConfig.toString()));
		this.jersey_handler = new NettyHttpHandler(application, resourceConfig);
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("initChannel(%s)", ch.toString()));
		}

		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("jersey-handler", jersey_handler);
	}

	public void reload()
	{
		jersey_handler.reload();
	}
}