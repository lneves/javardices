package org.caudexorigo.http.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class RedirectAction extends HttpAction {
  private final String path;

  private final HttpResponseStatus default_status;

  public RedirectAction(String path) {
    this(path, HttpResponseStatus.FOUND);
  }

  public RedirectAction(String path, HttpResponseStatus status) {
    super();
    this.path = path;

    if (status.getCode() < 300 || status.getCode() > 310) {
      throw new IllegalArgumentException("Invalid status for redirect handler");
    }

    this.default_status = status;
  }

  @Override
  public void service(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
    res.setStatus(this.default_status);
    res.addHeader(HttpHeaders.Names.LOCATION, path);
  }
}
