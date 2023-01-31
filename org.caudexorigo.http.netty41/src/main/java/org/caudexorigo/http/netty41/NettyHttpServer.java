package org.caudexorigo.http.netty41;

import java.net.InetSocketAddress;

import javax.net.ssl.SSLContext;

import org.caudexorigo.http.netty41.reporting.ResponseFormatter;
import org.caudexorigo.http.netty41.reporting.StandardResponseFormatter;
import org.caudexorigo.netty.DefaultNettyContext;
import org.caudexorigo.netty.NettyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

public class NettyHttpServer
{
	private static final String DEFAULT_HOST = "0.0.0.0";

	private static final int DEFAULT_PORT = 8080;

	private static final int DEFAULT_MAX_CONTENT_LENGHT = 1024 * 1024 * 4;

	private static Logger log = LoggerFactory.getLogger(NettyHttpServer.class);

	private String _host;

	private RequestRouter _mapper;
	private RequestObserver _requestObserver;
	private ResponseFormatter _rspFmt;

	private int _port;
	private int _maxContentLenght = -1;
	private boolean _validate_headers;
	private NettyContext _nettyCtx;

	public NettyHttpServer()
	{
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public NettyHttpServer(int port)
	{
		this(DEFAULT_HOST, port);
	}

	public NettyHttpServer(String host, int port)
	{
		_host = host;
		_port = port;
		_validate_headers = false;
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

	private boolean getValidateHeaders()
	{
		return _validate_headers;
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

	public ResponseFormatter getResponseFormtter()
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

	public RequestObserver getRequestObserver()
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

	public void setResponseFormatter(ResponseFormatter rspFmt)
	{
		_rspFmt = rspFmt;
	}

	public void setValidateHeaders(boolean validate_headers)
	{
		_validate_headers = validate_headers;
	}

	public void setMaxContentLenght(int maxContentLenght)
	{
		_maxContentLenght = maxContentLenght;
	}

	public synchronized void start()
	{
		log.info("Starting Httpd");

		NettyContext nctx = getNettyContext();
		EventLoopGroup bossGroup = nctx.getBossEventLoopGroup();
		EventLoopGroup workerGroup = nctx.getWorkerEventLoopGroup();

		try
		{
			Class<? extends ServerChannel> serverChannelClass = nctx.getServerChannelClass();

			NettyHttpServerInitializer server_init = new NettyHttpServerInitializer(_mapper, getRequestObserver(), getResponseFormtter(), getMaxContentLength(), getValidateHeaders());
			ServerBootstrap b = new ServerBootstrap();
			setupBootStrap(b);

			b.group(bossGroup, workerGroup).channel(serverChannelClass).childHandler(server_init);

			InetSocketAddress inet = new InetSocketAddress(_host, _port);
			log.info("Httpd started. Listening on {}:{}", _host, _port);
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

	public int getMaxContentLength()
	{
		if (_maxContentLenght > 0)
		{
			return _maxContentLenght;
		}
		else
		{
			return DEFAULT_MAX_CONTENT_LENGHT;
		}
	}

	public synchronized void startSsl(final HttpSslContext http_ssl_ctx)
	{
		log.info("Starting Httpd - SSL");

		NettyContext nctx = getNettyContext();
		EventLoopGroup bossGroup = nctx.getBossEventLoopGroup();
		EventLoopGroup workerGroup = nctx.getWorkerEventLoopGroup();

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

			Class<? extends ServerChannel> serverChannelClass = nctx.getServerChannelClass();

			b.group(bossGroup, workerGroup).channel(serverChannelClass).childHandler(server_init);

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
		bootstrap.childOption(ChannelOption.ALLOCATOR, getNettyContext().getAllocator());

		bootstrap.childOption(ChannelOption.MAX_MESSAGES_PER_READ, Integer.MAX_VALUE);
		bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
		bootstrap.option(ChannelOption.MAX_MESSAGES_PER_READ, Integer.MAX_VALUE);
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
	}

	private NettyContext getNettyContext()
	{
		if (_nettyCtx == null)
		{
			_nettyCtx = DefaultNettyContext.get();
		}
		return _nettyCtx;
	}

	public void setNettyContext(NettyContext nettyCtx)
	{
		_nettyCtx = nettyCtx;
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