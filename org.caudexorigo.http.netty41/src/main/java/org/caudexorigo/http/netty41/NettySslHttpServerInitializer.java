package org.caudexorigo.http.netty41;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;

public class NettySslHttpServerInitializer extends ChannelInitializer<SocketChannel>
{
	private final HttpProtocolHandler handler;
	private SSLContext ssl_context;

	public NettySslHttpServerInitializer(SSLContext ssl_context, HttpProtocolHandler handler)
	{
		super();
		this.ssl_context = ssl_context;
		this.handler = handler;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception
	{
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = ch.pipeline();

		final SSLEngine sslEngine = ssl_context.createSSLEngine();
		sslEngine.setUseClientMode(false);

		pipeline.addLast("ssl", new SslHandler(sslEngine));

		pipeline.addLast("decoder", new HttpRequestDecoder());

		pipeline.addLast("encoder", new HttpResponseEncoder());

		pipeline.addLast("handler", handler);
	}
}