package org.caudexorigo.http.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;

import org.caudexorigo.ErrorAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public class HttpProtocolHandler extends ChannelInboundHandlerAdapter
{
	private static Logger log = LoggerFactory.getLogger(HttpProtocolHandler.class);

	private final HttpAction defaultAction;

	private final RequestRouter _requestMapper;

	public HttpProtocolHandler(RequestRouter requestMapper, boolean useSsl)
	{
		this(null, requestMapper, useSsl);
	}

	public HttpProtocolHandler(URI root_directory, RequestRouter requestMapper, boolean useSsl)
	{
		_requestMapper = requestMapper;

		if (root_directory != null)
		{
			defaultAction = new StaticFileAction(root_directory, useSsl);
		}
		else
		{
			defaultAction = new DefaultAction();
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
	public void channelReadComplete(ChannelHandlerContext ctx)
	{
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof DefaultFullHttpRequest)
		{
			DefaultFullHttpRequest request = (DefaultFullHttpRequest) msg;

			try
			{
				HttpAction action = _requestMapper.map(ctx, request);

				if (action != null)
				{
					action.process(ctx, request);
				}
				else
				{
					defaultAction.process(ctx, request);
				}
			}
			catch (Throwable t)
			{
				try
				{
					ByteBuf buf = ctx.alloc().buffer();
					FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, buf);
					DefaultAction error_action = new DefaultAction();
					error_action.service(ctx, request, response);
					error_action.commitResponse(ctx, response, false);
				}
				catch (Throwable e)
				{
					log.error(e.getMessage(), e);
				}
			}
		}
	}
}