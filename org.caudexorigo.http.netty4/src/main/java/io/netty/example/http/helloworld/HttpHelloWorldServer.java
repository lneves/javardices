/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.http.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.commons.lang3.StringUtils;

/**
 * An HTTP server that sends back the content of the received HTTP request in a pretty plaintext form.
 */
public class HttpHelloWorldServer
{
	private static int IO_THREADS = Runtime.getRuntime().availableProcessors() * 2;
	private final int port;

	public HttpHelloWorldServer(int port)
	{
		this.port = port;
	}

	public void run() throws Exception
	{
		// Configure the server.
		if (Epoll.isAvailable())
		{
			System.out.println("netty-transport: linux-epoll");
			doRun(new EpollEventLoopGroup(), new EpollEventLoopGroup(), EpollServerSocketChannel.class);
		}
		else
		{
			System.out.println("netty-transport: nio");
			doRun(new NioEventLoopGroup(), new NioEventLoopGroup(), NioServerSocketChannel.class);
		}
	}

	private void doRun(EventLoopGroup bossGroup, EventLoopGroup workerGroup, Class<? extends ServerChannel> serverChannelClass) throws InterruptedException
	{
		try
		{
			String os_arch = System.getProperty("os.arch");
			boolean isARM = StringUtils.contains(os_arch, "arm");

			ServerBootstrap b = new ServerBootstrap();
			
			if (isARM)
			{
				b.childOption(ChannelOption.ALLOCATOR, new UnpooledByteBufAllocator(false));
			}
			else
			{
				b.childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true));
			}

			b.childOption(ChannelOption.MAX_MESSAGES_PER_READ, Integer.MAX_VALUE);
			b.childOption(ChannelOption.SO_REUSEADDR, true);
			b.option(ChannelOption.MAX_MESSAGES_PER_READ, Integer.MAX_VALUE);
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.option(ChannelOption.SO_REUSEADDR, true);

			b.group(bossGroup, workerGroup).channel(serverChannelClass).childHandler(new HttpHelloWorldServerInitializer());

			Channel ch = b.bind(port).sync().channel();

			System.out.printf("Httpd started. Listening on: %s%n", ch.localAddress().toString());

			ch.closeFuture().sync();
		}
		finally
		{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception
	{
		int port;
		if (args.length > 0)
		{
			port = Integer.parseInt(args[0]);
		}
		else
		{
			port = 8082;
		}
		new HttpHelloWorldServer(port).run();
	}
}