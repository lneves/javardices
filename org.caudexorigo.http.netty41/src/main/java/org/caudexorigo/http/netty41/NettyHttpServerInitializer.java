package org.caudexorigo.http.netty41;

import org.caudexorigo.http.netty41.reporting.ResponseFormatter;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel>
{
	private boolean validate_headers;
	private final RequestRouter mapper;
	private final RequestObserver requestObserver;
	private final ResponseFormatter responseFormatter;
	private int maxContentLength;

	public NettyHttpServerInitializer(RequestRouter mapper, RequestObserver requestObserver, ResponseFormatter responseFormatter, int maxLength, boolean validate_headers)
	{
		super();
		this.mapper = mapper;
		this.requestObserver = requestObserver;
		this.responseFormatter = responseFormatter;
		this.maxContentLength = maxLength;
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

		pipeline.addLast("http-encoder", new HttpResponseEncoder());
		pipeline.addLast("http-decoder", new HttpRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validate_headers));
		pipeline.addLast("http-aggregator", new HttpObjectAggregator(maxContentLength));

		// pipeline.addLast(new HttpServerCodec());
		// pipeline.addLast(new HttpObjectAggregator(65536));
		// pipeline.addLast(new ChunkedWriteHandler());

		pipeline.addLast("http-protocol-handler", new HttpProtocolHandler(mapper, requestObserver, responseFormatter));
	}
}