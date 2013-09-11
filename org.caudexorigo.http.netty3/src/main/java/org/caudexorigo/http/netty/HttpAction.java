package org.caudexorigo.http.netty;

import java.nio.charset.Charset;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.http.netty.reporting.StandardResponseFormatter;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpAction
{
	private static Logger log = LoggerFactory.getLogger(HttpAction.class);

	public abstract void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response);

	private HttpRequest request = null;

	public HttpAction()
	{
		super();
	}

	protected final void process(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		request = (this.request != null) ? this.request : request;

		if (this instanceof StaticFileAction)
		{
			try
			{
				service(ctx, request, response);
			}
			catch (Throwable ex)
			{
				handleError(request, response, ex);
				commitResponse(ctx, response, false);
			}
		}
		else
		{
			boolean is_keep_alive = HttpHeaders.isKeepAlive(request);
			if (!is_keep_alive)
			{
				response.setHeader(HttpHeaders.Names.CONNECTION, "Close");
			}

			try
			{
				HttpRequestWrapper hrw = null;
				if (request instanceof HttpRequestWrapper)
				{
					hrw = (HttpRequestWrapper) request;
				}
				else
				{
					hrw = new HttpRequestWrapper(request, getCharset());
				}

				service(ctx, hrw, response);
			}
			catch (Throwable ex)
			{
				handleError(request, response, ex);
			}
			finally
			{
				commitResponse(ctx, response, is_keep_alive);
			}
		}
	}

	private void handleError(HttpRequest request, HttpResponse response, Throwable ex)
	{
		request = (this.request != null) ? this.request : request;

		response.clearHeaders();
		Throwable rootCause = ErrorAnalyser.findRootCause(ex);

		if (ex instanceof WebException)
		{
			WebException w_ex = (WebException) ex;
			response.setStatus(HttpResponseStatus.valueOf(w_ex.getHttpStatusCode()));
		}

		if (log.isDebugEnabled())
		{
			log.error(rootCause.getMessage(), rootCause);
		}
		else
		{
			log.error("http.netty.error: {}; path: {}", rootCause.getMessage(), request.getUri());
		}

		writeStandardResponse(request, response, rootCause);
	}

	private void commitResponse(ChannelHandlerContext ctx, HttpResponse response, boolean is_keep_alive)
	{
		response.setHeader(HttpHeaders.Names.DATE, HttpDateFormat.getCurrentHttpDate());
		response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getContent().writerIndex()));
		Channel channel = ctx.getChannel();
		ChannelFuture future = channel.write(response);

		if (!is_keep_alive)
		{
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	protected void writeStandardResponse(HttpRequest request, HttpResponse response, Throwable rootCause)
	{
		request = (this.request != null) ? this.request : request;

		StandardResponseFormatter stdFrm = new StandardResponseFormatter(getShowFullErrorInfo());

		if (rootCause != null)
		{
			try
			{
				if (response.getStatus().getCode() < 400)
				{
					response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
				}

				stdFrm.formatResponse(request, response, rootCause);
			}
			catch (Throwable e)
			{
				log.error(e.getMessage(), e);
			}
		}
	}

	public void setRequest(HttpRequest request)
	{
		this.request = request;
	}

	public boolean getShowFullErrorInfo()
	{
		return false;
	}
	
	public Charset getCharset()
	{
		return null;
	}
}