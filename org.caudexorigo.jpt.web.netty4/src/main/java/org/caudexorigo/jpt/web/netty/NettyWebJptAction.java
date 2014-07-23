package org.caudexorigo.jpt.web.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.caudexorigo.http.netty4.HttpAction;
import org.caudexorigo.http.netty4.reporting.ResponseFormatter;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;
import org.caudexorigo.jpt.JptConfiguration;
import org.caudexorigo.jpt.JptInstance;
import org.caudexorigo.jpt.JptInstanceBuilder;
import org.caudexorigo.jpt.web.HttpJptContext;
import org.caudexorigo.jpt.web.HttpJptController;

public class NettyWebJptAction extends HttpAction
{
	private static final int[] NO_CONTENT_STATUS_CODES = new int[] { 204, 205, 301, 302, 303, 304, 305, 307 }; // code values must be ordered
	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

	private final URI _templateURI;
	private final ResponseFormatter _rspFormatter;

	public NettyWebJptAction(URI templateURI)
	{
		this(templateURI, JptConfiguration.fullErrors());
	}

	public NettyWebJptAction(URI templateURI, boolean showFullErrorInfo)
	{
		super();
		_templateURI = templateURI;
		System.out.println("NettyWebJptAction.NettyWebJptAction.templateURI: " + templateURI);
		_rspFormatter = new StandardResponseFormatter(showFullErrorInfo);
	}

	@Override
	public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		try
		{
			NettyJptProcessor aweb_jpt_processor = new NettyJptProcessor(ctx, request, response);
			JptInstance jpt = JptInstanceBuilder.getJptInstance(_templateURI);

			HttpJptContext jpt_ctx = new HttpJptContext(aweb_jpt_processor, getTemplateURI());
			HttpJptController page_controller = (HttpJptController) Class.forName(jpt.getCtxObjectType()).newInstance();

			page_controller.setHttpContext(jpt_ctx);
			page_controller.init();

			int http_status = jpt_ctx.getStatus();

			boolean allowsContent = allowsContent(http_status);

			if (allowsContent)
			{
				Map<String, Object> renderContext = new HashMap<String, Object>();
				renderContext.put("$this", page_controller);
				renderContext.put("$jpt", jpt_ctx);

				jpt.render(renderContext, aweb_jpt_processor.getWriter());
				aweb_jpt_processor.getWriter().flush();

				response.headers().set(HttpHeaders.Names.CONTENT_TYPE, CONTENT_TYPE);
			}
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	private static boolean allowsContent(int httpStatusCode)
	{
		for (int i = 0; i != NO_CONTENT_STATUS_CODES.length && NO_CONTENT_STATUS_CODES[i] <= httpStatusCode; ++i)
		{
			if (NO_CONTENT_STATUS_CODES[i] == httpStatusCode)
				return false;
		}

		return true;
	}

	public URI getTemplateURI()
	{
		return _templateURI;
	}

	@Override
	protected ResponseFormatter getResponseFormatter()
	{
		return _rspFormatter;
	}
}