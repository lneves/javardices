package org.caudexorigo.jpt.web.netty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import org.caudexorigo.http.netty.HttpRequestWrapper;
import org.caudexorigo.jpt.JptConfiguration;
import org.caudexorigo.jpt.web.HttpJptProcessor;
import org.caudexorigo.jpt.web.Method;
import org.caudexorigo.text.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class NettyJptProcessor implements HttpJptProcessor
{
	private static final String GZIP_ENCODING = "gzip";
	private static final String DEFLATE_ENCODING = "deflate";

	private final HttpRequestWrapper _req;

	private final HttpResponse _res;

	private Writer _writer;

	private OutputStream _out;

	private InputStream _in;

	private final String _encoding;

	private final ChannelHandlerContext _ctx;
	private boolean _use_compression;

	public NettyJptProcessor(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		this(ctx, request, response, false);
	}

	public NettyJptProcessor(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response, boolean use_compression)
	{
		_ctx = ctx;
		_use_compression = use_compression;
		try
		{
			_req = (HttpRequestWrapper) request;
			_res = response;
			ChannelBuffer buf = ChannelBuffers.dynamicBuffer();

			response.setContent(buf);
			_encoding = JptConfiguration.encoding();

			if (use_compression)
			{
				String accept_enconding = request.headers().get(HttpHeaders.Names.ACCEPT_ENCODING);
				boolean gzip_output = StringUtils.containsIgnoreCase(accept_enconding, GZIP_ENCODING);
				boolean deflate_output = StringUtils.containsIgnoreCase(accept_enconding, DEFLATE_ENCODING);

				if (gzip_output)
				{
					response.headers().set(HttpHeaders.Names.CONTENT_ENCODING, GZIP_ENCODING);
					_out = new GZIPOutputStream(new ChannelBufferOutputStream(buf), true);
				}
				else if (deflate_output)
				{
					response.headers().set(HttpHeaders.Names.CONTENT_ENCODING, DEFLATE_ENCODING);
					_out = new DeflaterOutputStream(new ChannelBufferOutputStream(buf), true);
				}
				else
				{
					_out = new ChannelBufferOutputStream(buf);
				}
			}
			else
			{
				_out = new ChannelBufferOutputStream(buf);
			}

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
		_res.headers().set(headerName, headerValue);
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
		return (InetSocketAddress) _ctx.getChannel().getLocalAddress();
	}

	@Override
	public InetSocketAddress getClientRemoteAddress()
	{
		return (InetSocketAddress) _ctx.getChannel().getRemoteAddress();
	}

	public void flush()
	{
		try
		{
			_writer.close();
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}
}