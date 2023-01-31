package org.caudexorigo.http.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;

public class DefaultRouter implements RequestRouter {
  private final HttpAction hello = new HelloWorldAction();

  @Override
  public HttpAction map(ChannelHandlerContext ctx, HttpRequest req) {
    if ("/hello".equals(req.getUri())) {
      return hello;
    }

    return null;
  }
}
