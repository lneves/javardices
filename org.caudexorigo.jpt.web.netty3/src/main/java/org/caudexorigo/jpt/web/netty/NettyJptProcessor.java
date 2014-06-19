package org.caudexorigo.jpt.web.netty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import org.caudexorigo.http.netty.HttpRequestWrapper;
import org.caudexorigo.jpt.JptConfiguration;
import org.caudexorigo.jpt.web.HttpJptProcessor;
import org.caudexorigo.jpt.web.Method;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class NettyJptProcessor implements HttpJptProcessor
{
	private final HttpRequestWrapper _req;

	private final HttpResponse _res;

	private Writer _writer;

	private OutputStream _out;

	private InputStream _in;

	private final String _encoding;

	private final ChannelHandlerContext ctx;

	public NettyJptProcessor(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		this.ctx = ctx;
		try
		{
			_req = (HttpRequestWrapper) request;
			_res = response;
			ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
			_out = new ChannelBufferOutputStream(buf);
			response.setContent(buf);
			_encoding = JptConfiguration.encoding();
			_writer = new OutputStreamWriter(_out, _encoding);

		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	@Override
	public String getRequestPath()
	{
		return _req.getUri();
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		if (_in == null)
		{
			_in = new ChannelBufferInputStream(_req.getContent());
		}
		return _in;
	}

	@Override
	public Method getMethod()
	{
		return Method.valueOf(_req.getMethod().toString());
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		return _out;
	}

	@Override
	public List<String> getParameters(String p_name)
	{
		return _req.getParameters(p_name);
	}

	@Override
	public final String getParameter(String p_name)
	{
		return _req.getParameter(p_name);
	}

	@Override
	public Map<String, List<String>> getParameters()
	{
		return _req.getParameters();
	}

	@Override
	public Object getSessionValue(String attr_name)
	{
		throw new UnsupportedOperationException("Sessions are not implemented when using the Netty HTTP Codec");
	}

	@Override
	public Writer getWriter() throws IOException
	{
		return _writer;
	}

	public void include(String uri)
	{
		throw new UnsupportedOperationException("Includes are not implemented when using the Netty HTTP Codec");
	}

	public void setSessionValue(String attr_name, Object value)
	{
		throw new UnsupportedOperationException("Sessions are not implemented when using the Netty HTTP Codec");
	}

	public void clearResponse()
	{
		_res.getContent().clear();
	}

	@Override
	public String getHeader(String headerName)
	{
		return _req.getHeader(headerName);
	}

	@Override
	public void setHeader(String headerName, String headerValue)
	{
		_res.setHeader(headerName, headerValue);
	}

	@Override
	public void setStatus(int statusCode)
	{
		_res.setStatus(HttpResponseStatus.valueOf(statusCode));
	}

	@Override
	public int getStatus()
	{
		return _res.getStatus().getCode();
	}

	@Override
	public InetSocketAddress getClientLocalAddress()
	{
		return (InetSocketAddress) ctx.getChannel().getLocalAddress();
	}

	@Override
	public InetSocketAddress getClientRemoteAddress()
	{
		return (InetSocketAddress) ctx.getChannel().getRemoteAddress();
	}
}