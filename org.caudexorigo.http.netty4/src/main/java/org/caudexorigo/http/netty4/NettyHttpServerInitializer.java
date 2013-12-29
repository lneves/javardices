package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel>
{
	private final HttpProtocolHandler handler;

	public NettyHttpServerInitializer(HttpProtocolHandler handler)
	{
		super();
		this.handler = handler;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception
	{
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = ch.pipeline();

		// Uncomment the following line if you want HTTPS
		// SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
		// engine.setUseClientMode(false);
		// pipeline.addLast("ssl", new SslHandler(engine));

		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		pipeline.addLast("encoder", new HttpResponseEncoder());

		pipeline.addLast("handler", handler); // Specify false if SSL.
	}
}