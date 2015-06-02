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
import io.netty.util.ReferenceCountUtil;

import org.caudexorigo.ErrorAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpAction
{
	private static final CharSequence CONTENT_LENGTH_ENTITY = HttpHeaders.newEntity(Names.CONTENT_LENGTH);
	private static final CharSequence DATE_ENTITY = HttpHeaders.newEntity(Names.DATE);

	private static Logger log = LoggerFactory.getLogger(HttpAction.class);

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

			try
			{
				service(ctx, request, response);
			}
			catch (Throwable t)
			{
				if (response != null)
				{
					ReferenceCountUtil.release(response);
				}
				throw new RuntimeException(t);
			}

			observeEnd(ctx, request, response, requestObserver);
			ReferenceCountUtil.release(response);
		}
		else
		{
			FullHttpResponse response = buildResponse(ctx);

			try
			{
				doProcess(ctx, request, response);
			}
			catch (Throwable t)
			{
				if (response != null)
				{
					ReferenceCountUtil.release(response);
				}
				throw new RuntimeException(t);
			}

			observeEnd(ctx, request, response, requestObserver);
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

	void commitResponse(ChannelHandlerContext ctx, FullHttpResponse response, boolean is_keep_alive)
	{

		response.headers().set(CONTENT_LENGTH_ENTITY, String.valueOf(response.content().readableBytes()));
		response.headers().set(DATE_ENTITY, HttpDateFormat.getCurrentHttpDate());

		ChannelFuture future = ctx.writeAndFlush(response); // implicit response.release();

		// Decide whether to close the connection or not.
		if (!is_keep_alive)
		{
			// Close the connection when the whole content is written out.
			future.addListener(ChannelFutureListener.CLOSE);
		}
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