package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface CacheKeyBuilder {
  public CacheKey build(ChannelHandlerContext ctx, FullHttpRequest request);
}
