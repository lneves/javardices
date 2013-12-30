package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import org.caudexorigo.http.netty4.reporting.ResponseFormatter;

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
		throw _ex;
	}

	@Override
	protected ResponseFormatter getResponseFormatter()
	{
		return _rspFmt;
	}
}