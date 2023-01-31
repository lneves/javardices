package org.caudexorigo.http.netty4;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.FileNotFoundException;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.http.netty4.reporting.ResponseFormatter;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;

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
			Throwable r = ErrorAnalyser.findRootCause(cause);
			log.debug(r.getMessage(), r);
		}

		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof FullHttpRequest)
		{
			FullHttpRequest request = (FullHttpRequest) msg;
			handleRead(ctx, request);

			try
			{
				ReferenceCountUtil.release(request);
			}
			catch (Throwable t)
			{
				log.error(t.getMessage(), t);
			}
		}
		else
		{
			throw new IllegalArgumentException("Can only handle instances of FullHttpRequest messages");
		}
	}

	public void handleRead(ChannelHandlerContext ctx, FullHttpRequest request)
	{
		if (is100ContinueExpected(request))
		{
			ctx.writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
		}

		try
		{
			if (request.getDecoderResult().isFailure())
			{
				if (request.getDecoderResult().cause() != null)
				{
					throw new WebException(request.getDecoderResult().cause(), HttpResponseStatus.BAD_REQUEST.code());
				}
				else
				{
					throw new WebException(new IllegalArgumentException(), HttpResponseStatus.BAD_REQUEST.code());
				}
			}
			
			HttpAction action = _requestMapper.map(ctx, request);

			if (action != null)
			{
				action.process(ctx, request, _requestObserver);
			}
			else
			{
				throw new WebException(new FileNotFoundException("No available HttpAction for the request"), HttpResponseStatus.NOT_FOUND.code());
			}
		}
		catch (Throwable t)
		{
			ByteBuf ebuf = ctx.alloc().buffer();
			FullHttpResponse eresponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, ebuf);

			Throwable w = findWebException(t);

			HttpAction errorAction;
			if (w instanceof WebException)
			{
				WebException we = (WebException) w;
				errorAction = new ErrorAction(we, _rspFmt);
				eresponse.setStatus(HttpResponseStatus.valueOf(we.getHttpStatusCode()));
			}
			else if (w instanceof IllegalArgumentException)
			{
				WebException we = new WebException(w, HttpResponseStatus.BAD_REQUEST.code());
				errorAction = new ErrorAction(we, _rspFmt);
				eresponse.setStatus(HttpResponseStatus.valueOf(we.getHttpStatusCode()));
			}
			else
			{
				Throwable r = ErrorAnalyser.findRootCause(t);
				errorAction = new ErrorAction(new WebException(r, HttpResponseStatus.INTERNAL_SERVER_ERROR.code()), _rspFmt);
			}

			errorAction.doProcess(ctx, request, eresponse);
			errorAction.observeEnd(ctx, request, eresponse, _requestObserver);
		}
	}

	private Throwable findWebException(Throwable ex)
	{
		if (ex instanceof WebException)
		{
			return ex;
		}
		else
		{
			Throwable inner_ex = ex.getCause();
			while (inner_ex != null)
			{
				if (inner_ex instanceof WebException)
				{
					return inner_ex;
				}
				inner_ex = inner_ex.getCause();
			}
		}

		return ErrorAnalyser.findRootCause(ex);
	}
}