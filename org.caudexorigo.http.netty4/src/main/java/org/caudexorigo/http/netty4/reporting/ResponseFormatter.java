package org.caudexorigo.http.netty4.reporting;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface ResponseFormatter {
  public abstract void formatResponse(ChannelHandlerContext ctx, FullHttpRequest request,
      FullHttpResponse response);

  public abstract void formatResponse(ChannelHandlerContext ctx, FullHttpRequest request,
      FullHttpResponse response, Throwable error);
}
