package org.caudexorigo.jersey;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class JerseyNettyResponseWriter implements ContainerResponseWriter
{
	private final HttpResponse response;

	public JerseyNettyResponseWriter(HttpResponse response)
	{
		super();
		this.response = response;
	}

	@Override
	public void commit()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean enableResponseBuffering()
	{
		return true;
	}

	@Override
	public void failure(Throwable error)
	{
		throw new RuntimeException(error);
	}

	@Override
	public void setSuspendTimeout(long timeOut, TimeUnit timeUnit) throws IllegalStateException
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean suspend(long timeOut, TimeUnit timeUnit, TimeoutHandler timeoutHandler)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OutputStream writeResponseStatusAndHeaders(long contentLength, ContainerResponse containerResponse) throws ContainerException
	{
		response.setStatus(HttpResponseStatus.valueOf(containerResponse.getStatus()));

		for (Map.Entry<String, List<Object>> e : containerResponse.getHeaders().entrySet())
		{
			List<String> values = new ArrayList<String>();
			for (Object v : e.getValue())
				values.add(containerResponse.getHeaderString((v.toString())));
			response.setHeader(e.getKey(), values);
		}

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		response.setContent(buffer);
		return new ChannelBufferOutputStream(buffer);
	}
}