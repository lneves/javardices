package org.caudexorigo.http.netty4;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.Shutdown;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

public class DefaultServer {
  public static void main(String[] args) {
    try {
      String host = "0.0.0.0";
      int port = 8000;

      InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.getDefaultFactory());
      NettyHttpServer server = new NettyHttpServer(host, port);

      server.setRequestObserver(new RequestObserver() {
        @Override
        public void begin(ChannelHandlerContext ctx, HttpRequest request) {
          request.headers().add("X-BEGIN", System.nanoTime());
        }

        @Override
        public void end(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {
          long elapsed = 0l;
          if (request.headers().get("X-BEGIN") != null) {
            long begin = Long.parseLong(request.headers().get("X-BEGIN"));
            elapsed = (System.nanoTime() - begin) / 1000000;
          }

          String path = request.getUri();

          String verb = request.getMethod().name();
          int status = response.getStatus().code();
          String remoteIp = StringUtils.substringBetween(ctx.channel().remoteAddress().toString(),
              "/", ":");

          String logLine = String.format("%s => %s \"%s %s\" - %s", elapsed, remoteIp, verb, path,
              status);
          System.out.println(logLine);
        }
      });

      server.setRouter(new DefaultRouter());
      server.setResponseFormatter(new StandardResponseFormatter(true));
      System.out.printf("listening on: %s%n", port);
      server.start();
    } catch (Throwable t) {
      Shutdown.now(t);
    }
  }
}
