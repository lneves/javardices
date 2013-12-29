package org.caudexorigo.http.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.net.URI;

import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHttpServer
{
	private static final String DEFAULT_HOST = "0.0.0.0";

	private static final int DEFAULT_PORT = 8080;

	private static Logger log = LoggerFactory.getLogger(NettyHttpServer.class);

	private String _host;

	private RequestRouter _mapper;

	private int _port;

	private URI _rootDirectory;

	public NettyHttpServer()
	{
		this(null);
	}

	public NettyHttpServer(URI root_directory)
	{
		_host = DEFAULT_HOST;
		_port = DEFAULT_PORT;
		_rootDirectory = root_directory;
	}

	public String getHost()
	{
		return _host;
	}

	public int getPort()
	{
		return _port;
	}

	public RequestRouter getRouter()
	{
		return _mapper;
	}

	public void setHost(String host)
	{
		_host = host;
	}

	public void setPort(int port)
	{
		_port = port;
	}

	public void setRouter(RequestRouter mapper)
	{
		_mapper = mapper;
	}

	public synchronized void start()
	{
		log.info("Starting Httpd");

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try
		{
			HttpProtocolHandler http_handler = new HttpProtocolHandler(_rootDirectory, _mapper, false);
			NettyHttpServerInitializer server_init = new NettyHttpServerInitializer(http_handler);
			ServerBootstrap b = new ServerBootstrap();
			setupBootStrap(b);
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(server_init);

			InetSocketAddress inet = new InetSocketAddress("0.0.0.0", _port);
			log.info("Httpd started. Listening on port: {}", _port);
			b.bind(inet).sync().channel().closeFuture().sync();			
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
		finally
		{
			stop(bossGroup, workerGroup);
		}
	}

	public synchronized void startSsl(final HttpSslContext http_ssl_ctx)
	{
		log.info("Starting Httpd - SSL");

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		final SSLContext sslContext;
		try
		{
			sslContext = http_ssl_ctx.getSSLContext();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		try
		{
			HttpProtocolHandler http_handler = new HttpProtocolHandler(_rootDirectory, _mapper, true);
			NettySslHttpServerInitializer server_init = new NettySslHttpServerInitializer(sslContext, http_handler);
			ServerBootstrap b = new ServerBootstrap();
			setupBootStrap(b);
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(server_init);

			b.bind(http_ssl_ctx.getSslPort()).sync().channel().closeFuture().sync();
			log.info("Httpd SSL started. Listening on port: {}", http_ssl_ctx.getSslPort());
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
		finally
		{
			stop(bossGroup, workerGroup);
		}
	}

	private void setupBootStrap(ServerBootstrap bootstrap)
	{
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.childOption(ChannelOption.SO_RCVBUF, 128 * 1024);
		bootstrap.childOption(ChannelOption.SO_SNDBUF, 128 * 1024);

		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
	}

	private void stop(EventLoopGroup bossGroup, EventLoopGroup workerGroup)
	{
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
        
        // Wait until all threads are terminated.
        try
		{
			bossGroup.terminationFuture().sync();
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
        try
		{
			workerGroup.terminationFuture().sync();
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}
}