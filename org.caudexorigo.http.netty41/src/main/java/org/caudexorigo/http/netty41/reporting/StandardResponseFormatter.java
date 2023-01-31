package org.caudexorigo.http.netty41.reporting;

import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.io.UnsynchronizedStringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;

public class StandardResponseFormatter implements ResponseFormatter
{
	private static Logger log = LoggerFactory.getLogger(StandardResponseFormatter.class);

	private final boolean showFullErrorInfo;

	public StandardResponseFormatter(boolean showFullErrorInfo)
	{
		super();

		this.showFullErrorInfo = showFullErrorInfo;
	}

	@Override
	public void formatResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		formatResponse(ctx, request, response, null);
	}

	@Override
	public void formatResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response, Throwable error)
	{
		if (MessageBody.allow(response.status().code()))
		{
			try
			{
				Throwable root = ErrorAnalyser.findRootCause(error);
				String html = String.format(ErrorTemplates.getTemplate(response.status().code()), response.status().code(), response.status().reasonPhrase(), request.method().toString(), getStackTrace(root, showFullErrorInfo));
				byte[] bytes = html.toString().getBytes("UTF-8");

				response.content().writeBytes(bytes);
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
				logError(root);
			}
			catch (Throwable t)
			{
				logError(t);
			}
		}
	}

	private void logError(Throwable t)
	{
		if (showFullErrorInfo)
		{
			log.error(getMsg(t), t);
		}
		else
		{
			log.error(getMsg(t));
		}
	}

	public String getStackTrace(Throwable error, boolean fullInfo)
	{
		if (error != null)
		{
			if (fullInfo)
			{
				UnsynchronizedStringWriter sw = new UnsynchronizedStringWriter();
				PrintWriter pw = new PrintWriter(sw);
				error.printStackTrace(pw);
				pw.flush();
				return sw.toString();
			}
			else
			{
				return getMsg(error);
			}
		}
		else
		{
			return "N/A";
		}
	}

	private String getMsg(Throwable error)
	{
		return StringUtils.defaultString(error.getMessage(), "N/A");
	}
}
