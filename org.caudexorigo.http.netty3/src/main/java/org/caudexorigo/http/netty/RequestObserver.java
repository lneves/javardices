package org.caudexorigo.http.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public interface RequestObserver {
  public void begin(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response);

  public void end(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response);
}
