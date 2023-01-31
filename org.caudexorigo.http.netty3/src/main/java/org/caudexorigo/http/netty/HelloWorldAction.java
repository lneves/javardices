package org.caudexorigo.http.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class HelloWorldAction extends HttpAction {
  private static final byte[] hello = "Hello World".getBytes();

  private static ChannelBuffer content = ChannelBuffers.wrappedBuffer(hello);

  @Override
  public void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {
    response.setContent(content.duplicate());
  }
}
