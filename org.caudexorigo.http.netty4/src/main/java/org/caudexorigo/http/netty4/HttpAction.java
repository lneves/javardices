package org.caudexorigo.http.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpAction
{
	private static Logger log = LoggerFactory.getLogger(HttpAction.class);

	public abstract void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response);

	public HttpAction()
	{
		super();
	}

	protected final void process(ChannelHandlerContext ctx, FullHttpRequest request)
	{
		if (this instanceof StaticFileAction)
		{
			System.err.println("HttpAction.process(StaticFileAction)");
			try
			{
				service(ctx, request, null);
			}
			catch (Throwable ex)
			{
				handleError(ctx, request, ex);
			}
		}
		else
		{
			try
			{
				ByteBuf buf = ctx.alloc().buffer();
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

				service(ctx, request, response);
				boolean is_keep_alive = HttpHeaders.isKeepAlive(request);
				commitResponse(ctx, response, is_keep_alive);
			}
			catch (Throwable ex)
			{
				handleError(ctx, request, ex);
			}
		}
	}

	private void handleError(ChannelHandlerContext ctx, FullHttpRequest request, Throwable ex)
	{
		Throwable rootCause = ErrorAnalyser.findRootCause(ex);

		ByteBuf buf = ctx.alloc().buffer();
		FullHttpResponse response;

		if (ex instanceof WebException)
		{
			WebException w_ex = (WebException) ex;
			HttpResponseStatus error_status = HttpResponseStatus.valueOf(w_ex.getHttpStatusCode());
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, error_status, buf);
		}
		else
		{
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, buf);
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
		commitResponse(ctx, response, false);
	}

	void commitResponse(ChannelHandlerContext ctx, FullHttpResponse response, boolean is_keep_alive)
	{
		response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.content().readableBytes()));
		response.headers().set(HttpHeaders.Names.DATE, HttpDateFormat.getCurrentHttpDate());
		Channel channel = ctx.channel();
		ChannelFuture future = channel.write(response);

		if (!is_keep_alive)
		{
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	protected void writeStandardResponse(FullHttpRequest request, FullHttpResponse response, Throwable rootCause)
	{
		StandardResponseFormatter stdFrm = new StandardResponseFormatter(getShowFullErrorInfo());

		if (rootCause != null)
		{
			try
			{
				if (response.getStatus().code() < 400)
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

	public boolean getShowFullErrorInfo()
	{
		return false;
	}
}