/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package io.netty.example.http.helloworld;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AsciiString;

@Sharable
public class HttpHelloWorldServerHandler extends ChannelInboundHandlerAdapter {
  private static final CharSequence CONTENT_LENGTH_ENTITY = new AsciiString(
      HttpHeaderNames.CONTENT_LENGTH);
  private static final CharSequence CONNECTION_ENTITY = new AsciiString(HttpHeaderNames.CONNECTION);
  private static final CharSequence CONTENT_TYPE_ENTITY = new AsciiString(
      HttpHeaderNames.CONTENT_TYPE);

  private static final CharSequence KEEP_ALIVE = new AsciiString(HttpHeaderValues.KEEP_ALIVE);
  private static final CharSequence TEXT_PLAIN = new AsciiString("text/plain");

  private static final byte[] CONTENT = "Hello World".getBytes();

  private static final ByteBuf CONTENT_BUFFER = Unpooled.unreleasableBuffer(Unpooled.directBuffer()
      .writeBytes(CONTENT));
  private static final CharSequence contentLength = new AsciiString(String.valueOf(CONTENT_BUFFER
      .readableBytes()));

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof HttpRequest) {
      HttpRequest req = (HttpRequest) msg;

      if (is100ContinueExpected(req)) {
        ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
      }

      boolean keepAlive = isKeepAlive(req);
      FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, CONTENT_BUFFER
          .duplicate(), false);
      HttpHeaders hh = response.headers();
      hh.set(CONTENT_TYPE_ENTITY, TEXT_PLAIN);
      hh.set(CONTENT_LENGTH_ENTITY, contentLength);

      if (!keepAlive) {
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
      } else {
        hh.set(CONNECTION_ENTITY, KEEP_ALIVE);
        ctx.write(response);
      }
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    // cause.printStackTrace();
    ctx.close();
  }
}
