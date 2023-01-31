package org.caudexorigo.http.netty;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.http.netty.reporting.ResponseFormatter;
import org.caudexorigo.http.netty.reporting.StandardResponseFormatter;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public abstract class HttpAction {
  private static Logger log = LoggerFactory.getLogger(HttpAction.class);

  public abstract void service(ChannelHandlerContext ctx, HttpRequest request,
      HttpResponse response);

  private HttpRequest request = null;
  private ResponseFormatter defaultRspFmt = new StandardResponseFormatter(false);

  public HttpAction() {
    super();
  }

  protected final void process(ChannelHandlerContext ctx, HttpRequest request,
      HttpResponse response) {
    request = (this.request != null) ? this.request : request;

    if (this instanceof StaticFileAction) {
      try {
        service(ctx, request, response);
      } catch (Throwable ex) {
        handleError(ctx, request, response, ex);
        commitResponse(ctx, response, false);
      }
    } else {
      boolean is_keep_alive = HttpHeaders.isKeepAlive(request);
      if (!is_keep_alive) {
        response.headers().set(HttpHeaders.Names.CONNECTION, "Close");
      }

      try {
        HttpRequestWrapper hrw = null;
        if (request instanceof HttpRequestWrapper) {
          hrw = (HttpRequestWrapper) request;
        } else {
          hrw = new HttpRequestWrapper(request, getCharset());
        }

        service(ctx, hrw, response);
      } catch (Throwable ex) {
        handleError(ctx, request, response, ex);
      } finally {
        commitResponse(ctx, response, is_keep_alive);
      }
    }
  }

  private void handleError(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response,
      Throwable ex) {
    request = (this.request != null) ? this.request : request;

    response.headers().clear();
    Throwable rootCause = ErrorAnalyser.findRootCause(ex);

    if (ex instanceof WebException) {
      WebException w_ex = (WebException) ex;
      response.setStatus(HttpResponseStatus.valueOf(w_ex.getHttpStatusCode()));
    }

    if (log.isDebugEnabled()) {
      log.error(rootCause.getMessage(), rootCause);
    } else {
      log.error("http.netty.error: {}; path: {}", rootCause.getMessage(), request.getUri());
    }

    writeStandardResponse(ctx, request, response, rootCause);
  }

  private void commitResponse(ChannelHandlerContext ctx, HttpResponse response,
      boolean is_keep_alive) {
    response.headers().set(HttpHeaders.Names.DATE, HttpDateFormat.getCurrentHttpDate());
    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getContent()
        .writerIndex()));
    Channel channel = ctx.getChannel();
    ChannelFuture future = channel.write(response);

    if (!is_keep_alive) {
      future.addListener(ChannelFutureListener.CLOSE);
    }
  }

  private void writeStandardResponse(ChannelHandlerContext ctx, HttpRequest request,
      HttpResponse response, Throwable rootCause) {
    request = (this.request != null) ? this.request : request;

    ResponseFormatter rspFrm = getResponseFormatter();

    if (rootCause != null) {
      try {
        if (response.getStatus().getCode() < 400) {
          response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }

        rspFrm.formatResponse(ctx, request, response, rootCause);
      } catch (Throwable e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  public void setRequest(HttpRequest request) {
    this.request = request;
  }

  public Charset getCharset() {
    return null;
  }

  protected ResponseFormatter getResponseFormatter() {
    return defaultRspFmt;
  }
}
