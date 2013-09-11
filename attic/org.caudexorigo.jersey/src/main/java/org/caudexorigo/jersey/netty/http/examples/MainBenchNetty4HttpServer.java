package org.caudexorigo.jersey.netty.http.examples;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoop;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.net.InetSocketAddress;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.jersey.netty.http.HttpDateFormat;

public class MainBenchNetty4HttpServer
{
	public static void main(String[] args)
	{
		try
		{
			InetSocketAddress inet = new InetSocketAddress("0.0.0.0", 8585);

			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.option(ChannelOption.SO_BACKLOG, 16384);

			System.out.printf("Httpd started. Listening on port: %s%n", inet.toString());

			bootstrap.eventLoop(new NioEventLoop(), new NioEventLoop()).channel(new NioServerSocketChannel()).localAddress(inet).childHandler(new HttpServerInitializer());

			bootstrap.bind().sync().channel().closeFuture().sync();
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);
			r.printStackTrace();
		}

	}

	private static class HttpServerInitializer extends ChannelInitializer<SocketChannel>
	{
		@Override
		public void initChannel(SocketChannel ch) throws Exception
		{
			System.err.printf("MainBenchNetty4HttpServer.HttpServerInitializer.initChannel(%s)%n", ch.toString());
			// Create a default pipeline implementation.
			ChannelPipeline pipeline = ch.pipeline();

			// Uncomment the following line if you want HTTPS
			// SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
			// engine.setUseClientMode(false);
			// pipeline.addLast("ssl", new SslHandler(engine));

			pipeline.addLast("decoder", new HttpRequestDecoder());
			pipeline.addLast("encoder", new HttpResponseEncoder());
			pipeline.addLast("bench", new BenchHandler());
		}
	}

	private static class BenchHandler extends ChannelInboundMessageHandlerAdapter<HttpRequest>
	{
		private static final String SERVER_NAME = "netty4";
		private static final String LM = "Mon, 28 Sep 1970 06:00:00 GMT";

		@Override
		public void messageReceived(ChannelHandlerContext ctx, HttpRequest request) throws Exception
		{
			HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

			try
			{
				response.setStatus(HttpResponseStatus.OK);
				response.setHeader(HttpHeaders.Names.SERVER, SERVER_NAME);
				response.setHeader(HttpHeaders.Names.DATE, HttpDateFormat.getCurrentHttpDate());
				response.setHeader(HttpHeaders.Names.LAST_MODIFIED, LM);

				boolean is_keep_alive = HttpHeaders.isKeepAlive(request);
				if (is_keep_alive)
				{
					response.setHeader(HttpHeaders.Names.CONNECTION, "Keep-Alive");
				}

				response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, "43");
				response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "image/gif");

				response.setContent(BenchImage.getChannelBuffer());

				ChannelFuture future = ctx.write(response);

				if (!is_keep_alive)
				{
					future.addListener(ChannelFutureListener.CLOSE);
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				throw new RuntimeException(t);
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
		{
			Channel ch = ctx.channel();
			ch.close();
		}
	}
}