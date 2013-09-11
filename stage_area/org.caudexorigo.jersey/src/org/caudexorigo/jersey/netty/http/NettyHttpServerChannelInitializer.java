package org.caudexorigo.jersey.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.glassfish.grizzly.http.Method;
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
	
	
	private SslHandler getSslHandler() 
	{
		NettyHttpSslContext SSLctx = new NettyHttpSslContext(8383, "ssl/server.jks", "123456789", "123456789");
		SSLContext ssl = null;
		try {
			ssl = SSLctx.getSSLContext();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		SSLEngine engine = ssl.createSSLEngine();
		engine.setUseClientMode(false);
		return new SslHandler(engine);
	}
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception
	{
		
		
		
		System.err.printf("NettyHttpServerChannelInitializer.initChannel(%s)%n", ch.toString());
		
		if (log.isDebugEnabled())
		{
			log.debug(String.format("initChannel(%s)", ch.toString()));
		}
		
		ChannelPipeline pipeline = ch.pipeline();
		
		//pipeline.addLast("ssl", getSslHandler());
		
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("jersey-handler", jersey_handler);
		
		if (log.isDebugEnabled())
		{
			log.debug(String.format("channel initiated"));
			
		}
	}

	public void reload()
	{
		jersey_handler.reload();
	}
}