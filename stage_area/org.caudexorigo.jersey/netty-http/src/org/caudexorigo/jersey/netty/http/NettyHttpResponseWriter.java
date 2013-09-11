package org.caudexorigo.jersey.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHttpResponseWriter implements ContainerResponseWriter
{
	private static Logger log = LoggerFactory.getLogger(NettyHttpResponseWriter.class);

	private Channel channel;

	private final HttpRequest request;
	private final HttpResponse response;

	public NettyHttpResponseWriter(Channel channel, HttpRequest request, HttpResponse response)
	{
		this.channel = channel;
		this.request = request;
		this.response = response;

		if (log.isDebugEnabled())
		{
			log.debug(String.format("new (%s, %s, %s)", channel.toString(), request, response));
		}
	}

	@Override
	public void cancel()
	{
		log.debug("cancel()");
	}

	@Override
	public void commit()
	{
		boolean is_keep_alive = HttpHeaders.isKeepAlive(request);
		if (is_keep_alive)
		{
			response.setHeader(HttpHeaders.Names.CONNECTION, "Keep-Alive");
		}

		ChannelFuture chf = channel.write(response);

		if (!is_keep_alive)
		{
			chf.addListener(ChannelFutureListener.CLOSE);
		}

		log.debug("commit()");
	}

	@Override
	public void setSuspendTimeout(long time, TimeUnit tu) throws IllegalStateException
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("setSuspendTimeout(%s, %s)", time, tu));
		}
	}

	@Override
	public void suspend(long time, TimeUnit tu, TimeoutHandler to_handler) throws IllegalStateException
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("suspend(%s, %s)", time, tu, to_handler.toString()));
		}
	}

	@Override
	public OutputStream writeResponseStatusAndHeaders(long contentLength, ContainerResponse context) throws ContainerException
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("writeResponseStatusAndHeaders(%s, %s)", contentLength, context.toString()));
		}

		response.setStatus(HttpResponseStatus.valueOf(context.getStatus()));

		final List<String> values = new ArrayList<String>();
		for (Map.Entry<String, List<Object>> header : context.getHeaders().entrySet())
		{

			for (Object value : header.getValue())
			{
				values.add(value.toString());
			}
			response.setHeader(header.getKey(), values);
			values.clear();
		}
		response.setChunked(false);
		HttpHeaders.setContentLength(response, contentLength);
		setDateHeader(response);
		ByteBuf buf = Unpooled.directBuffer();
		response.setContent(buf);
		ByteBufOutputStream b = new ByteBufOutputStream(buf);
		return b;
	}

	private static void setDateHeader(HttpResponse response)
	{
		response.setHeader(HttpHeaders.Names.DATE, HttpDateFormat.getCurrentHttpDate());
	}
}