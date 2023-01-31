package org.caudexorigo.http.netty41;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class DefaultAction extends HttpAction {
  public void service(ChannelHandlerContext ctx, FullHttpRequest request,
      FullHttpResponse response) {
    response.setStatus(HttpResponseStatus.OK);
  }
}
