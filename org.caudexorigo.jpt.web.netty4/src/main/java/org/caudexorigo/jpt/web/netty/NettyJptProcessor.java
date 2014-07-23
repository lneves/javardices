package org.caudexorigo.jpt.web.netty;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import org.caudexorigo.http.netty4.ParameterDecoder;
import org.caudexorigo.jpt.JptConfiguration;
import org.caudexorigo.jpt.web.HttpJptProcessor;
import org.caudexorigo.jpt.web.Method;
import org.caudexorigo.text.StringUtils;

public class NettyJptProcessor implements HttpJptProcessor
{
	private static final String GZIP_ENCODING = "gzip";
	private static final String DEFLATE_ENCODING = "deflate";

	private final FullHttpRequest _req;

	private final FullHttpResponse _res;

	private Writer _writer;

	private OutputStream _out;

	private InputStream _in;

	private final ChannelHandlerContext ctx;

	private final ParameterDecoder parameterDecoder;

	public NettyJptProcessor(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		this(ctx, request, response, false);
	}

	public NettyJptProcessor(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response, boolean use_compression)
	{
		this.ctx = ctx;
		try
		{
			_req = request;
			_res = response;

			_out = new ByteBufOutputStream(response.content());

			String charsetName = JptConfiguration.encoding();
			parameterDecoder = new ParameterDecoder(_req, Charset.forName(charsetName));

			if (use_compression)
			{
				String accept_enconding = request.headers().get(HttpHeaders.Names.ACCEPT_ENCODING);
				boolean gzip_output = StringUtils.containsIgnoreCase(accept_enconding, GZIP_ENCODING);
				boolean deflate_output = StringUtils.containsIgnoreCase(accept_enconding, DEFLATE_ENCODING);

				if (gzip_output)
				{
					response.headers().set(HttpHeaders.Names.CONTENT_ENCODING, GZIP_ENCODING);
					_writer = new OutputStreamWriter(new GZIPOutputStream(_out, true), charsetName);
				}
				else if (deflate_output)
				{
					response.headers().set(HttpHeaders.Names.CONTENT_ENCODING, DEFLATE_ENCODING);
					_writer = new OutputStreamWriter(new DeflaterOutputStream(_out, true), charsetName);
				}
				else
				{
					_writer = new OutputStreamWriter(_out, charsetName);
				}
			}
			else
			{
				_writer = new OutputStreamWriter(_out, charsetName);
			}

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
			_in = new ByteBufInputStream(_req.content());
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
		return parameterDecoder.getParameters(p_name);
	}

	@Override
	public final String getParameter(String p_name)
	{
		return parameterDecoder.getParameter(p_name);
	}

	@Override
	public Map<String, List<String>> getParameters()
	{
		return parameterDecoder.getParameters();
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
		_res.content().clear();
	}

	@Override
	public String getHeader(String headerName)
	{
		return _req.headers().get(headerName);
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
		return _res.getStatus().code();
	}

	@Override
	public InetSocketAddress getClientLocalAddress()
	{
		return (InetSocketAddress) ctx.channel().localAddress();
	}

	@Override
	public InetSocketAddress getClientRemoteAddress()
	{
		return (InetSocketAddress) ctx.channel().remoteAddress();
	}
}