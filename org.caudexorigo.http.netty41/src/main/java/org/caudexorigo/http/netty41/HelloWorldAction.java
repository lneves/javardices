package org.caudexorigo.http.netty41;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class HelloWorldAction extends HttpAction {
  private byte[] hello = "Hello World".getBytes();

  public HelloWorldAction() {
    super();
  }

  @Override
  public void service(ChannelHandlerContext ctx, FullHttpRequest request,
      FullHttpResponse response) {
    response.content().writeBytes(hello);
  }
}
