package org.caudexorigo.http.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import javax.net.ssl.SSLContext;

import org.caudexorigo.http.netty4.reporting.ResponseFormatter;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHttpServer
{
	private static final String DEFAULT_HOST = "0.0.0.0";

	private static final int DEFAULT_PORT = 8080;

	private static Logger log = LoggerFactory.getLogger(NettyHttpServer.class);

	private String _host;

	private RequestRouter _mapper;
	private RequestObserver _requestObserver;
	private ResponseFormatter _rspFmt;

	private int _port;
	private final boolean _is_compression_enabled;

	public NettyHttpServer()
	{
		this(DEFAULT_HOST, DEFAULT_PORT, false);
	}

	public NettyHttpServer(int port)
	{
		this(DEFAULT_HOST, port, false);
	}

	public NettyHttpServer(String host, int port)
	{
		this(host, port, false);
	}

	public NettyHttpServer(String host, int port, boolean is_compression_enabled)
	{
		_host = host;
		_port = port;
		_is_compression_enabled = is_compression_enabled;

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

	private ResponseFormatter getResponseFormtter()
	{
		if (_rspFmt != null)
		{
			return _rspFmt;
		}
		else
		{
			return new StandardResponseFormatter(false);
		}
	}

	protected RequestObserver getRequestObserver()
	{
		if (_requestObserver != null)
		{
			return _requestObserver;
		}
		else
		{
			return new DefaultObserver();
		}
	}

	public void setRequestObserver(RequestObserver requestObserver)
	{
		_requestObserver = requestObserver;
	}

	public void setResponseFormtter(ResponseFormatter rspFmt)
	{
		_rspFmt = rspFmt;
	}

	public synchronized void start()
	{
		log.info("Starting Httpd");

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try
		{
			HttpProtocolHandler http_handler = new HttpProtocolHandler(_mapper, getRequestObserver(), getResponseFormtter());
			NettyHttpServerInitializer server_init = new NettyHttpServerInitializer(http_handler, _is_compression_enabled);
			ServerBootstrap b = new ServerBootstrap();
			setupBootStrap(b);
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(server_init);

			InetSocketAddress inet = new InetSocketAddress(_host, _port);
			log.info("Httpd started. Listening on {}:{}", _host, _port);
			System.out.printf("Httpd started. Listening on %s:%s%n", _host, _port);
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
			HttpProtocolHandler http_handler = new HttpProtocolHandler(_mapper, getRequestObserver(), getResponseFormtter());
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