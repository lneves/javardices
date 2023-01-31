package org.caudexorigo.jpt.web.netty;

import org.caudexorigo.http.netty41.HttpAction;
import org.caudexorigo.http.netty41.reporting.MessageBody;
import org.caudexorigo.jpt.JptInstance;
import org.caudexorigo.jpt.JptInstanceBuilder;
import org.caudexorigo.jpt.web.HttpJptContext;
import org.caudexorigo.jpt.web.HttpJptController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

public class NettyWebJptAction extends HttpAction {
  private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

  private final URI _templateURI;

  public NettyWebJptAction(URI templateURI) {
    super();
    _templateURI = templateURI;
  }

  @Override
  public void service(ChannelHandlerContext ctx, FullHttpRequest request,
      FullHttpResponse response) {
    try {
      NettyJptProcessor aweb_jpt_processor = new NettyJptProcessor(ctx, request, response);
      JptInstance jpt = JptInstanceBuilder.getJptInstance(_templateURI);

      HttpJptContext jpt_ctx = new HttpJptContext(aweb_jpt_processor, getTemplateURI());
      HttpJptController page_controller = (HttpJptController) Class.forName(jpt.getCtxObjectType())
          .newInstance();

      page_controller.setHttpContext(jpt_ctx);
      page_controller.init();

      int http_status = jpt_ctx.getStatus();

      boolean allowsContent = MessageBody.allow(http_status);

      if (allowsContent) {
        Map<String, Object> renderContext = new HashMap<String, Object>();
        renderContext.put("$this", page_controller);
        renderContext.put("$jpt", jpt_ctx);

        jpt.render(renderContext, aweb_jpt_processor.getWriter());
        aweb_jpt_processor.flush();

        if (!response.headers().contains(HttpHeaders.Names.CONTENT_TYPE)) {
          response.headers().set(HttpHeaders.Names.CONTENT_TYPE, CONTENT_TYPE);
        }
      }
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public URI getTemplateURI() {
    return _templateURI;
  }
}
