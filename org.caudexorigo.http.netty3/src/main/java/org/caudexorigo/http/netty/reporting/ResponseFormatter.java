package org.caudexorigo.http.netty.reporting;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public interface ResponseFormatter {
  public abstract void formatResponse(ChannelHandlerContext ctx, HttpRequest request,
      HttpResponse response);

  public abstract void formatResponse(ChannelHandlerContext ctx, HttpRequest request,
      HttpResponse response, Throwable error);
}
