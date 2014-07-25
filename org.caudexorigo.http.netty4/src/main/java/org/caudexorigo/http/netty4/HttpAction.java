package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.nio.charset.Charset;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.http.netty4.reporting.ResponseFormatter;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpAction
{
	private static final CharSequence CONTENT_LENGTH_ENTITY = HttpHeaders.newEntity(Names.CONTENT_LENGTH);
	private static final CharSequence DATE_ENTITY = HttpHeaders.newEntity(Names.DATE);
	private static Logger log = LoggerFactory.getLogger(HttpAction.class);

	private ResponseFormatter defaultRspFmt = new StandardResponseFormatter(false);
	private boolean useCompression;
	private boolean useCache;

	public abstract void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response);

	public HttpAction()
	{
		this(false, false);
	}
	
	public HttpAction(boolean useCompression, boolean useCache)
	{
		super();
		this.useCompression = useCompression;
		this.useCache = useCache;
	}

	protected final void process(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		if (response == null)
		{
			try
			{
				service(ctx, request, null);
			}
			catch (Throwable ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			boolean is_keep_alive = HttpHeaders.isKeepAlive(request);

			try
			{
				service(ctx, request, response);
			}
			catch (Throwable ex)
			{
				handleError(ctx, request, response, ex);
			}
			finally
			{
				commitResponse(ctx, response, is_keep_alive);
			}
		}
	}

	private void handleError(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response, Throwable ex)
	{
		response.headers().clear();
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

	void commitResponse(ChannelHandlerContext ctx, FullHttpResponse response, boolean is_keep_alive)
	{
		response.headers().set(CONTENT_LENGTH_ENTITY, String.valueOf(response.content().readableBytes()));
		response.headers().set(DATE_ENTITY, HttpDateFormat.getCurrentHttpDate());
		ChannelFuture future = ctx.writeAndFlush(response);

		// Decide whether to close the connection or not.
		if (!is_keep_alive)
		{
			// Close the connection when the whole content is written out.
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	protected void writeStandardResponse(FullHttpRequest request, FullHttpResponse response, Throwable rootCause)
	{
		ResponseFormatter rspFrm = getResponseFormatter();

		if (rootCause != null)
		{
			try
			{
				if (response.getStatus().code() < 400)
				{
					response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
				}

				rspFrm.formatResponse(request, response, rootCause);
			}
			catch (Throwable e)
			{
				log.error(e.getMessage(), e);
			}
		}
	}
	
	

	public Charset getCharset()
	{
		return null;
	}

	protected ResponseFormatter getResponseFormatter()
	{
		return defaultRspFmt;
	}
}