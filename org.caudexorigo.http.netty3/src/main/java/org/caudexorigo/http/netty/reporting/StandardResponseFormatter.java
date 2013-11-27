package org.caudexorigo.http.netty.reporting;

import java.io.PrintWriter;

import org.caudexorigo.io.UnsynchronizedStringWriter;
import org.caudexorigo.text.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.caudexorigo.http.netty.reporting.ResponseFormtter#formatResponse(org.jboss.netty.handler.codec.http.HttpRequest, org.jboss.netty.handler.codec.http.HttpResponse)
	 */
	@Override
	public void formatResponse(HttpRequest request, HttpResponse response)
	{
		formatResponse(request, response, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.caudexorigo.http.netty.reporting.ResponseFormtter#formatResponse(org.jboss.netty.handler.codec.http.HttpRequest, org.jboss.netty.handler.codec.http.HttpResponse, java.lang.Throwable)
	 */
	@Override
	public void formatResponse(HttpRequest request, HttpResponse response, Throwable error)
	{
		int rsp_code = response.getStatus().getCode();

		if (MessageBody.allow(rsp_code))
		{
			try
			{
				String html = String.format(ErrorTemplates.getTemplate(response.getStatus().getCode()), response.getStatus().getCode(), response.getStatus().getReasonPhrase(), request.getMethod().toString(), getStackTrace(error, showFullErrorInfo));
				byte[] bytes = html.toString().getBytes("UTF-8");
				ChannelBuffer out = ChannelBuffers.wrappedBuffer(bytes);
				response.setContent(out);
				response.addHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html");
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
				log.error(error.getMessage(), error);
				UnsynchronizedStringWriter sw = new UnsynchronizedStringWriter();
				PrintWriter pw = new PrintWriter(sw);
				error.printStackTrace(pw);
				pw.flush();
				String emsg = sw.toString();
				return emsg;
			}
			else
			{
				return StringUtils.defaultIfEmpty(error.getMessage(), "N/A");
			}
		}
		else
		{
			return "N/A";
		}
	}
}
