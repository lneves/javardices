package org.caudexorigo.http.netty4.reporting;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

import java.io.PrintWriter;

import org.caudexorigo.io.UnsynchronizedStringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public void formatResponse(FullHttpRequest request, FullHttpResponse response)
	{
		formatResponse(request, response, null);
	}

	@Override
	public void formatResponse(FullHttpRequest request, FullHttpResponse response, Throwable error)
	{
		if (MessageBody.allow(response.getStatus().code()))
		{
			try
			{
				String html = String.format(ErrorTemplates.getTemplate(response.getStatus().code()), response.getStatus().code(), response.getStatus().reasonPhrase(), request.getMethod().toString(), getStackTrace(error, showFullErrorInfo));
				byte[] bytes = html.toString().getBytes("UTF-8");

				response.content().writeBytes(bytes);
				response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html");
			}
			catch (Throwable t)
			{
				log.error(t.getMessage(), t);
			}
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
				return error.getMessage();
			}
		}
		else
		{
			return "N/A";
		}
	}
}
