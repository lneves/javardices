package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public interface RequestObserver {
  public void begin(ChannelHandlerContext ctx, HttpRequest request);

  public void end(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response);
}
