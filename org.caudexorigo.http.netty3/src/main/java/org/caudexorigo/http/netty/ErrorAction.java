package org.caudexorigo.http.netty;

import org.caudexorigo.http.netty.reporting.ResponseFormatter;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class ErrorAction extends HttpAction {
  private final WebException _ex;
  private final ResponseFormatter _rspFmt;

  public ErrorAction(WebException ex, ResponseFormatter rspFmt) {
    super();
    _ex = ex;
    _rspFmt = rspFmt;
  }

  @Override
  public void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {
    throw _ex;
  }

  @Override
  protected ResponseFormatter getResponseFormatter() {
    return _rspFmt;
  }
}
