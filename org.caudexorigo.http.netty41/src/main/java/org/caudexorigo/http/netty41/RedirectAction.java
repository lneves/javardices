package org.caudexorigo.http.netty41;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;

public class RedirectAction extends HttpAction {
  private final String path;

  private final HttpResponseStatus default_status;

  public RedirectAction(String path) {
    this(path, HttpResponseStatus.FOUND);
  }

  public RedirectAction(String path, HttpResponseStatus status) {
    super();
    this.path = path;

    if (status.code() < 300 || status.code() > 310) {
      throw new IllegalArgumentException("Invalid status for redirect handler");
    }

    this.default_status = status;
  }

  @Override
  public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse rsp) {
    rsp.setStatus(this.default_status);
    rsp.headers().add(HttpHeaderNames.LOCATION, path);
  }
}
