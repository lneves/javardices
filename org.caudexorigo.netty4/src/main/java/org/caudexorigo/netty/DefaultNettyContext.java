package org.caudexorigo.netty;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
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

public class DefaultNettyContext implements NettyContext
{
	private static final DefaultNettyContext instance = new DefaultNettyContext();

	private final ByteBufAllocator _allocator;

	private final Class<? extends ServerChannel> _serverChannelClass;
	private final Class<? extends Channel> _channelClass;
	private final Class<? extends DatagramChannel> _datagramChannelClass;

	private final EventLoopGroup _bossEventLoopGroup;
	private final EventLoopGroup _workerEventLoopGroup;

	private DefaultNettyContext()
	{
		super();

		String os_arch = System.getProperty("os.arch");

		boolean isARM = contains(os_arch, "arm");

		if (isARM)
		{
			_allocator = UnpooledByteBufAllocator.DEFAULT;
		}
		else
		{
			_allocator = PooledByteBufAllocator.DEFAULT;
		}

		if (Epoll.isAvailable())
		{
			_bossEventLoopGroup = new EpollEventLoopGroup();
			_workerEventLoopGroup = new EpollEventLoopGroup();
			_serverChannelClass = EpollServerSocketChannel.class;
			_channelClass = EpollSocketChannel.class;
			_datagramChannelClass = EpollDatagramChannel.class;
		}
		else
		{
			_bossEventLoopGroup = new NioEventLoopGroup();
			_workerEventLoopGroup = new NioEventLoopGroup();
			_serverChannelClass = NioServerSocketChannel.class;
			_channelClass = NioSocketChannel.class;
			_datagramChannelClass = NioDatagramChannel.class;
		}
	}

	@Override
	public ByteBufAllocator getAllocator()
	{
		return _allocator;
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
	public EventLoopGroup getBossEventLoopGroup()
	{
		return _bossEventLoopGroup;
	}

	@Override
	public EventLoopGroup getWorkerEventLoopGroup()
	{
		return _workerEventLoopGroup;
	}

	@Override
	public Class<? extends DatagramChannel> getDatagramChannelClass()
	{
		return _datagramChannelClass;
	}

	private static final boolean contains(String instr, String searchstr)
	{
		if ((instr != null) && (instr.trim().length() > 0))
		{
			return instr.contains(searchstr);
		}
		else
		{
			return false;
		}
	}

	public static final NettyContext get()
	{
		return instance;
	}
}
