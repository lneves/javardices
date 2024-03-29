package org.caudexorigo.jpt.web.netty;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.reporting.ResponseFormatter;
import org.caudexorigo.http.netty.reporting.StandardResponseFormatter;
import org.caudexorigo.jpt.JptConfiguration;
import org.caudexorigo.jpt.JptInstance;
import org.caudexorigo.jpt.JptInstanceBuilder;
import org.caudexorigo.jpt.web.HttpJptContext;
import org.caudexorigo.jpt.web.HttpJptController;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class NettyWebJptAction extends HttpAction {
  private static final int[] NO_CONTENT_STATUS_CODES = new int[] {204, 205, 301, 302, 303, 304, 305,
      307}; // code values must be ordered
  private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
  private static final JptConfiguration jptConf = JptConfiguration.fromFile("/jpt.config");

  private final URI _templateURI;
  private final StandardResponseFormatter _rspFormatter;
  private boolean _compress;

  public NettyWebJptAction(URI templateURI) {
    this(templateURI, jptConf.fullErrors());
  }

  public NettyWebJptAction(URI templateURI, boolean showFullErrorInfo) {
    this(templateURI, showFullErrorInfo, false);
  }

  public NettyWebJptAction(URI templateURI, boolean showFullErrorInfo, boolean compress) {
    super();
    _templateURI = templateURI;
    _rspFormatter = new StandardResponseFormatter(showFullErrorInfo);
    _compress = compress;
  }

  @Override
  public void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {
    try {
      NettyJptProcessor aweb_jpt_processor = new NettyJptProcessor(ctx, request, response,
          _compress);
      JptInstance jpt = JptInstanceBuilder.getJptInstance(_templateURI);

      HttpJptContext jpt_ctx = new HttpJptContext(aweb_jpt_processor, getTemplateURI());
      HttpJptController page_controller = (HttpJptController) Class.forName(jpt.getCtxObjectType())
          .newInstance();

      page_controller.setHttpContext(jpt_ctx);
      page_controller.init();

      int http_status = jpt_ctx.getStatus();

      boolean allowsContent = allowsContent(http_status);

      if (allowsContent) {
        Map<String, Object> renderContext = new HashMap<String, Object>();
        renderContext.put("$this", page_controller);
        renderContext.put("$jpt", jpt_ctx);

        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, CONTENT_TYPE);

        jpt.render(renderContext, aweb_jpt_processor.getWriter());
        aweb_jpt_processor.flush();
      }
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  private static boolean allowsContent(int httpStatusCode) {
    for (int i = 0; i != NO_CONTENT_STATUS_CODES.length
        && NO_CONTENT_STATUS_CODES[i] <= httpStatusCode; ++i) {
      if (NO_CONTENT_STATUS_CODES[i] == httpStatusCode)
        return false;
    }

    return true;
  }

  public URI getTemplateURI() {
    return _templateURI;
  }

  @Override
  protected ResponseFormatter getResponseFormatter() {
    return _rspFormatter;
  }
}
