package org.caudexorigo.http.netty4;

import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class TestForm extends HttpAction {
  @Override
  public void service(ChannelHandlerContext ctx, FullHttpRequest request,
      FullHttpResponse response) {
    NettyRequest req = new NettyRequest(request);
    Map<String, List<String>> params = req.getParameters();

    System.out.println(params);
  }
}
