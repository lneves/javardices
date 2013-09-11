package org.caudexorigo.wstest.srv;

import java.io.IOException;
import java.util.Set;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.io.IOUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseHandler extends HttpAction
{
	private static final Logger log = LoggerFactory.getLogger(BaseHandler.class);

	@Override
	public final void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		HttpServiceResponse srvResponse = null;

		try
		{
			if (!isAllowedMethod(request))
			{
				throw new WsException(new IllegalArgumentException("Method Not Allowed"), 405);
			}
			else
			{
				srvResponse = handleRequest(request);
			}
		}
		catch (WsException error)
		{
			srvResponse = handleFault(request, "Client", error);

		}
		catch (Throwable error)
		{
			srvResponse = handleFault(request, "Server", error);
		}

		sendServiceResponse(srvResponse, response);
	}

	protected abstract HttpServiceResponse handleRequest(HttpRequest request) throws Throwable;

	protected abstract HttpMethod[] getAllowedMethods(HttpRequest request);

	public abstract String getContentType();

	public abstract HttpServiceResponse buildFaultMessageResponse(HttpRequest request, String faultCode, Throwable ex);

	private final HttpServiceResponse handleFault(HttpRequest request, String faultCode, Throwable ex)
	{
		HttpServiceResponse faultResponse = buildFaultMessageResponse(request, faultCode, ex);

		logError(request, ex);

		return faultResponse;
	}

	private void logError(HttpRequest request, Throwable error)
	{
		try
		{
			if (log.isDebugEnabled())
			{
				// log to console
				ChannelBuffer bb = request.getContent();
				bb.setIndex(0, bb.writerIndex());
				ChannelBufferInputStream input = new ChannelBufferInputStream(bb);
				String s = IOUtils.toString(input);
				log.debug("Failed to process message.", error);
				log.debug(String.format("Received message: '%s'", s));
			}
		}
		catch (IOException e)
		{
			log.error("Failed to get received message when processing error", error);
		}
	}

	private void sendServiceResponse(HttpServiceResponse srvResponse, HttpResponse response)
	{
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE, getContentType());

		if (srvResponse.hasHeaders())
		{
			Set<String> headers = srvResponse.getHeaders().keySet();
			for (String header : headers)
			{
				response.setHeader(header, srvResponse.getHeader(header));
			}
		}
		response.setStatus(HttpResponseStatus.valueOf(srvResponse.getHttpStatus()));
		response.setContent(srvResponse.getResult());
	}

	private boolean isAllowedMethod(HttpRequest request)
	{
		HttpMethod[] amethods = getAllowedMethods(request);

		for (HttpMethod amethod : amethods)
		{
			if (request.getMethod() == amethod)
			{
				return true;
			}
		}

		return false;
	}
}