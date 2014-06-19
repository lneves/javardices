package org.caudexorigo.http.netty4;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.FileNotFoundException;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.http.netty4.reporting.ResponseFormatter;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
// public class HttpProtocolHandler
public class HttpProtocolHandler extends ChannelInboundHandlerAdapter
{
	private static Logger log = LoggerFactory.getLogger(HttpProtocolHandler.class);

	private final ResponseFormatter _rspFmt;
	private final RequestRouter _requestMapper;
	private final RequestObserver _requestObserver;

	public HttpProtocolHandler(RequestRouter requestMapper)
	{
		_requestMapper = requestMapper;
		_rspFmt = new StandardResponseFormatter(false);
		_requestObserver = new DefaultObserver();
	}

	public HttpProtocolHandler(RequestRouter requestMapper, RequestObserver customObserver, ResponseFormatter customResponseFormtter)
	{
		_requestMapper = requestMapper;

		if (customResponseFormtter == null)
		{
			_rspFmt = new StandardResponseFormatter(false);
		}
		else
		{
			_rspFmt = customResponseFormtter;
		}

		if (customObserver == null)
		{
			_requestObserver = new DefaultObserver();
		}
		else
		{
			_requestObserver = customObserver;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		if (log.isDebugEnabled())
		{
			Throwable rootCause = ErrorAnalyser.findRootCause(cause);
			log.debug(rootCause.getMessage(), rootCause);
		}

		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof DefaultFullHttpRequest)
		{
			DefaultFullHttpRequest request = (DefaultFullHttpRequest) msg;
			handleRead(ctx, request);
		}
	}

	public void handleRead(ChannelHandlerContext ctx, DefaultFullHttpRequest request)
	{
		if (is100ContinueExpected(request))
		{
			ctx.write(new DefaultHttpResponse(HTTP_1_1, CONTINUE));
		}

		HttpAction action = _requestMapper.map(ctx, request);

		try
		{
			observeBegin(ctx, request);

			if (action != null)
			{
				if (action instanceof StaticFileAction)
				{
					action.process(ctx, request, null);
				}
				else
				{
					ByteBuf buf = ctx.alloc().buffer();
					FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
					action.process(ctx, request, response);
				}
			}
			else
			{
				throw new WebException(new FileNotFoundException("No available HttpAction for the request"), HttpResponseStatus.NOT_FOUND.code());
			}
		}
		catch (Throwable t)
		{
			HttpAction errorAction;
			if (t instanceof WebException)
			{
				WebException we = (WebException) t;
				errorAction = new ErrorAction(we, _rspFmt);
			}
			else
			{
				errorAction = new ErrorAction(new WebException(t, HttpResponseStatus.INTERNAL_SERVER_ERROR.code()), _rspFmt);
			}

			ByteBuf ebuf = ctx.alloc().buffer();
			FullHttpResponse eresponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, ebuf);

			errorAction.process(ctx, request, eresponse);
			observeEnd(ctx, request, eresponse);
		}
	}

	private void observeBegin(ChannelHandlerContext ctx, HttpRequest request)
	{
		try
		{
			_requestObserver.begin(ctx, request);
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);
			log.error(r.getMessage(), r);
		}
	}

	private void observeEnd(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		try
		{
			_requestObserver.end(ctx, request, response);
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);
			log.error(r.getMessage(), r);
		}
	}
}