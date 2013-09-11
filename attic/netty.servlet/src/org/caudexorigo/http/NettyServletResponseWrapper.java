package org.caudexorigo.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.HttpDateFormat;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class NettyServletResponseWrapper implements HttpServletResponse
{
	private final ChannelBufferOutputStream buf;
	private final ServletOutputStream output_stream;
	private final HttpAction http_action;
	private String characterEncoding;
	private final HttpResponse http_response;

	public NettyServletResponseWrapper(HttpAction http_action, ChannelHandlerContext ctx, HttpResponse http_response)
	{
		this.http_action = http_action;
		this.http_response = http_response;
		buf = new ChannelBufferOutputStream(http_response.getContent());

		output_stream = new ServletOutputStream()
		{
			@Override
			public void write(int b) throws IOException
			{
				output_stream.write(b);

			}
		};

		characterEncoding = "UTF-8";
	}

	@Override
	public void flushBuffer() throws IOException
	{
		output_stream.flush();

	}

	@Override
	public int getBufferSize()
	{
		return buf.buffer().capacity();
	}

	@Override
	public String getCharacterEncoding()
	{
		return characterEncoding;
	}

	@Override
	public String getContentType()
	{
		return http_response.getHeader(HttpHeaders.Names.CONTENT_TYPE);
	}

	@Override
	public Locale getLocale()
	{
		return Locale.getDefault();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException
	{
		return output_stream;
	}

	@Override
	public PrintWriter getWriter() throws IOException
	{
		return new PrintWriter(buf);
	}

	@Override
	public boolean isCommitted()
	{
		return false;
	}

	@Override
	public void reset()
	{
		http_response.clearHeaders();
		buf.buffer().clear();
	}

	@Override
	public void resetBuffer()
	{
		buf.buffer().clear();
	}

	@Override
	public void setBufferSize(int size)
	{
		// NOP
	}

	@Override
	public void setCharacterEncoding(String characterEncoding)
	{
		this.characterEncoding = characterEncoding;
	}

	@Override
	public void setContentLength(int len)
	{
		http_response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, len);
	}

	@Override
	public void setContentType(String type)
	{
		http_response.setHeader(HttpHeaders.Names.CONTENT_TYPE, type);
	}

	@Override
	public void setLocale(Locale locale)
	{
		throw new UnsupportedOperationException("setLocale is not implemented");
	}

	@Override
	public void addCookie(Cookie cookie)
	{
		throw new UnsupportedOperationException("addCookie is not implemented");
	}

	@Override
	public void addDateHeader(String name, long epoch)
	{
		http_response.addHeader(name, HttpDateFormat.getHttpDate(new Date(epoch)));
	}

	@Override
	public void addHeader(String name, String value)
	{
		http_response.addHeader(name, value);

	}

	@Override
	public void addIntHeader(String name, int value)
	{
		http_response.addHeader(name, value);
	}

	@Override
	public boolean containsHeader(String name)
	{
		return http_response.containsHeader(name);
	}

	@Override
	public String encodeRedirectURL(String url)
	{
		return encodeURL(url);
	}

	@Override
	public String encodeRedirectUrl(String url)
	{
		return encodeURL(url);
	}

	@Override
	public String encodeURL(String url)
	{
		try
		{
			return URLEncoder.encode(url, characterEncoding);
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	@Override
	public String encodeUrl(String url)
	{
		return encodeURL(url);
	}

	@Override
	public void sendError(int sc) throws IOException
	{
		http_response.setStatus(HttpResponseStatus.valueOf(sc));

	}

	@Override
	public void sendError(int sc, String message) throws IOException
	{
		reset();
		http_response.setStatus(HttpResponseStatus.valueOf(sc));
		output_stream.print(message);
	}

	@Override
	public void sendRedirect(String location) throws IOException
	{
		http_response.setStatus(HttpResponseStatus.FOUND);
		http_response.addHeader(HttpHeaders.Names.LOCATION, location);
	}

	@Override
	public void setDateHeader(String name, long epoch)
	{
		http_response.setHeader(name, HttpDateFormat.getHttpDate(new Date(epoch)));
	}

	@Override
	public void setHeader(String name, String value)
	{
		http_response.setHeader(name, value);
	}

	@Override
	public void setIntHeader(String name, int value)
	{
		http_response.setHeader(name, value);
	}

	@Override
	public void setStatus(int sc)
	{
		http_response.setStatus(HttpResponseStatus.valueOf(sc));
	}

	@Override
	public void setStatus(int sc, String message)
	{
		http_response.setStatus(HttpResponseStatus.valueOf(sc));
	}

}
