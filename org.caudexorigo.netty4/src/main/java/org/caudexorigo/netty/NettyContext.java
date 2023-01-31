package org.caudexorigo.netty;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.DatagramChannel;

public interface NettyContext {
  public ByteBufAllocator getAllocator();

  public Class<? extends ServerChannel> getServerChannelClass();

  public Class<? extends Channel> getChannelClass();

  public Class<? extends DatagramChannel> getDatagramChannelClass();

  public EventLoopGroup getBossEventLoopGroup();

  public EventLoopGroup getWorkerEventLoopGroup();

}
