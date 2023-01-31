package org.caudexorigo.http.netty41;

import org.caudexorigo.http.netty41.reporting.ResponseFormatter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class ErrorAction extends HttpAction
{
	private final WebException _ex;
	private final ResponseFormatter _rspFmt;

	public ErrorAction(WebException ex, ResponseFormatter rspFmt)
	{
		super();
		_ex = ex;
		_rspFmt = rspFmt;
	}

	@Override
	public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		_rspFmt.formatResponse(ctx, request, response, _ex);
	}

	public WebException getError()
	{
		return _ex;
	}
}