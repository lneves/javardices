package org.caudexorigo.jersey.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoop;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NettyHttpServer
{
	private static Logger log = LoggerFactory.getLogger(NettyHttpResponseWriter.class);

	private static final ChannelGroup ALL_CHANNELS = new DefaultChannelGroup("jersey_netty_server");

	private ServerBootstrap bootstrap;
	private final SocketAddress localSocket;
	private final ChannelInitializer<SocketChannel> channelInit;

	/*
	 * TODO - SecurityContext
 	 */
	
	public NettyHttpServer(final ChannelInitializer<SocketChannel> channelInit, final SocketAddress localSocket)
	{
		this.localSocket = localSocket;

		this.channelInit = channelInit;
		this.bootstrap = buildBootstrap();

		if (log.isDebugEnabled())
		{
			log.debug(String.format("new (%s, %s)", channelInit.toString(), localSocket.toString()));
		}
	}

	public void reload()
	{
		((NettyHttpServerChannelInitializer) channelInit).reload();
	}

	public void startServer() throws InterruptedException
	{
		log.debug("startServer()");
		final ChannelFuture serverChannel = bootstrap.bind().sync().channel().closeFuture();
		ALL_CHANNELS.add(serverChannel.channel());
	}

	public void stopServer()
	{
		log.debug("stopServer()");
		final ChannelGroupFuture future = ALL_CHANNELS.close();
		future.awaitUninterruptibly();
		bootstrap.shutdown();
		ALL_CHANNELS.clear();
	}

	private ServerBootstrap buildBootstrap()
	{
		bootstrap = new ServerBootstrap();
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		bootstrap.option(ChannelOption.SO_BACKLOG, 16384);
		bootstrap.eventLoop(new NioEventLoop(), new NioEventLoop()).channel(new NioServerSocketChannel()).localAddress(localSocket).childHandler(channelInit);
		return bootstrap;
	}
}