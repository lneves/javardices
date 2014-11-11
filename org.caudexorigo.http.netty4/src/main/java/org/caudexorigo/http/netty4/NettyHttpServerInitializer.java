package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import org.caudexorigo.http.netty4.reporting.ResponseFormatter;

public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel>
{
	private boolean validate_headers;
	private RequestRouter mapper;
	private RequestObserver requestObserver;
	private ResponseFormatter responseFormatter;
	private static final int MAX_CONTENT_LENGHT = 1024 * 1012 * 4;

	public NettyHttpServerInitializer(RequestRouter mapper, RequestObserver requestObserver, ResponseFormatter responseFormatter, boolean validate_headers)
	{
		super();
		this.mapper = mapper;
		this.requestObserver = requestObserver;
		this.responseFormatter = responseFormatter;
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

		pipeline.addLast("http-decoder", new HttpRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validate_headers));
		pipeline.addLast("http-aggregator", new HttpObjectAggregator(MAX_CONTENT_LENGHT));
		pipeline.addLast("http-encoder", new HttpResponseEncoder());

		// pipeline.addLast(new HttpServerCodec());
		// pipeline.addLast(new HttpObjectAggregator(65536));
		// pipeline.addLast(new ChunkedWriteHandler());


		pipeline.addLast("http-protocol-handler", new HttpProtocolHandler(mapper, requestObserver, responseFormatter));
		
		
	}
}