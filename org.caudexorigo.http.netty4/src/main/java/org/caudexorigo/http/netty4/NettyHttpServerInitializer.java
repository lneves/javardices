package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel>
{
	private final HttpProtocolHandler handler;
	private boolean is_compression_enabled;
	private boolean validate_headers;

	public NettyHttpServerInitializer(HttpProtocolHandler handler, boolean is_compression_enabled, boolean validate_headers)
	{
		super();
		this.handler = handler;
		this.is_compression_enabled = is_compression_enabled;
		this.validate_headers = validate_headers;
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

		int maxInitialLineLength = 4096;
		int maxHeaderSize = 8192;
		int maxChunkSize = 8192;

		pipeline.addLast("decoder", new HttpRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validate_headers));
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		pipeline.addLast("encoder", new HttpResponseEncoder());

		if (is_compression_enabled)
		{
			pipeline.addLast("http-compression", new HttpContentCompressor());
		}

		pipeline.addLast("handler", handler);
	}
}