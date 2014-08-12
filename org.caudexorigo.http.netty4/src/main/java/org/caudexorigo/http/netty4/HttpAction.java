package org.caudexorigo.http.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

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

	public HttpAction()
	{
		super();
	}

	public abstract void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response);

	protected void process(ChannelHandlerContext ctx, FullHttpRequest request, RequestObserver requestObserver)
	{
		observeBegin(ctx, request, requestObserver);

		if (isZeroCopy())
		{
			ByteBuf buf = new EmptyByteBuf(ctx.alloc());
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
			service(ctx, request, response);
			observeEnd(ctx, request, response, requestObserver);
		}
		else
		{
			FullHttpResponse response = buildResponse(ctx);

			try
			{
				doProcess(ctx, request, response);
				observeEnd(ctx, request, response, requestObserver);
			}
			catch (Throwable ex)
			{
				handleError(ctx, request, ex, requestObserver);
			}
		}
	}

	void doProcess(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		boolean is_keep_alive = HttpHeaders.isKeepAlive(request);
		service(ctx, request, response);
		commitResponse(ctx, response, is_keep_alive);
	}

	protected FullHttpResponse buildResponse(ChannelHandlerContext ctx)
	{
		ByteBuf buf = ctx.alloc().buffer();
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		return response;
	}

	protected boolean isZeroCopy()
	{
		return false;
	}

	void handleError(ChannelHandlerContext ctx, FullHttpRequest request, Throwable ex, RequestObserver requestObserver)
	{
		ByteBuf ebuf = ctx.alloc().buffer();
		FullHttpResponse eresponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, ebuf);

		Throwable error_ex = new Exception(ex);
		while (error_ex.getCause() != null)
		{
			error_ex = error_ex.getCause();
			if (error_ex instanceof WebException)
			{
				WebException w_ex = (WebException) error_ex;
				eresponse.setStatus(HttpResponseStatus.valueOf(w_ex.getHttpStatusCode()));
				break;
			}
		}

		writeStandardResponse(request, eresponse, error_ex);
		commitResponse(ctx, eresponse, false);
		observeEnd(ctx, request, eresponse, requestObserver);
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

	protected ResponseFormatter getResponseFormatter()
	{
		return defaultRspFmt;
	}

	protected void observeBegin(ChannelHandlerContext ctx, HttpRequest request, RequestObserver requestObserver)
	{
		try
		{
			requestObserver.begin(ctx, request);
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);
			log.error(r.getMessage(), r);
		}
	}

	protected void observeEnd(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response, RequestObserver requestObserver)
	{
		try
		{
			requestObserver.end(ctx, request, response);
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);
			log.error(r.getMessage(), r);
		}
	}
}