package org.caudexorigo.netty;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class VoidNettyContext implements NettyContext
{
	private final Class<? extends ServerChannel> _serverChannelClass;
	private final Class<? extends Channel> _channelClass;
	private final Class<? extends DatagramChannel> _datagramChannelClass;
	
	public VoidNettyContext()
	{
		super();
		if (Epoll.isAvailable())
		{
			_serverChannelClass = EpollServerSocketChannel.class;
			_channelClass = EpollSocketChannel.class;
			_datagramChannelClass = EpollDatagramChannel.class;
		}
		else
		{
			_serverChannelClass = NioServerSocketChannel.class;
			_channelClass = NioSocketChannel.class;
			_datagramChannelClass = NioDatagramChannel.class;
		}
	}

	@Override
	public ByteBufAllocator getAllocator()
	{
		return UnpooledByteBufAllocator.DEFAULT;
	}

	@Override
	public Class<? extends ServerChannel> getServerChannelClass()
	{
		return _serverChannelClass;
	}

	@Override
	public Class<? extends Channel> getChannelClass()
	{
		return _channelClass;
	}

	@Override
	public Class<? extends DatagramChannel> getDatagramChannelClass()
	{
		return _datagramChannelClass;
	}

	@Override
	public EventLoopGroup getBossEventLoopGroup()
	{
		return null;
	}

	@Override
	public EventLoopGroup getWorkerEventLoopGroup()
	{
		return null;
	}
}