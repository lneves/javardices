package org.caudexorigo.http.netty4;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.http.netty4.reporting.ResponseFormatter;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;
import org.caudexorigo.text.UrlCodec;

public class StaticFileAction extends HttpAction
{
	private final File rootDirectory;
	private final String rootDirectoryPath;

	private final long cacheAge;
	private ResponseFormatter rspFmt;

	public StaticFileAction(URI rootPath)
	{
		this(rootPath, new StandardResponseFormatter(false), 0);
	}

	public StaticFileAction(URI rootPath, ResponseFormatter rspFmt)
	{
		this(rootPath, rspFmt, 0);
	}

	public StaticFileAction(URI root_path, ResponseFormatter rspFmt, long cacheAge)
	{
		this.rspFmt = rspFmt;
		this.rootDirectory = new File(root_path);
		this.rootDirectoryPath = rootDirectory.getAbsolutePath();

		this.cacheAge = cacheAge;

		if (!rootDirectory.isDirectory() || !rootDirectory.canRead() || rootDirectory.isHidden())
		{
			throw new IllegalArgumentException("Not a valid root directory");
		}
	}

	@Override
	public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse rsp)
	{
		validateRequest(request);

		// rsp is null at this time
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);

		File file = getFile(request);

		String abs_path = getFileAbsolutePath(file);

		long clen = file.length();

		response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, Long.toString(clen));

		CharSequence ctype = MimeTable.getContentType(abs_path);
		if (StringUtils.isNotBlank(ctype))
		{
			response.headers().set(HttpHeaders.Names.CONTENT_TYPE, ctype);
		}

		if (StringUtils.isNotBlank(getContentEncoding()))
		{
			response.headers().set(HttpHeaders.Names.CONTENT_ENCODING, getContentEncoding());
		}

		RandomAccessFile raf = null;
		try
		{
			raf = new RandomAccessFile(file, "r");
		}
		catch (FileNotFoundException e1)
		{
			// this exception can be ignored because we already tested for it
		}

		response.setStatus(HttpResponseStatus.OK);
		response.headers().set(HttpHeaders.Names.DATE, HttpDateFormat.getCurrentHttpDate());

		if (cacheAge > 0)
		{
			response.headers().set(HttpHeaders.Names.CACHE_CONTROL, String.format("max-age=%s", cacheAge));
			response.headers().set(HttpHeaders.Names.LAST_MODIFIED, HttpDateFormat.getHttpDate(new Date(file.lastModified())));
		}

		boolean is_keep_alive = HttpHeaders.isKeepAlive(request);

		ctx.write(response);

		try
		{
			boolean useSsl = (ctx.pipeline().get(SslHandler.class) != null);

			System.out.println("StaticFileAction.service.useSsl: " + useSsl);

			if (useSsl)
			{
				is_keep_alive = false;
				ctx.write(new ChunkedFile(raf, 0, clen, 8192 * 2), ctx.voidPromise());
			}
			else
			{
				ctx.write(new DefaultFileRegion(raf.getChannel(), 0, clen), ctx.voidPromise());
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		// Write the end marker
		ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

		// Decide whether to close the connection or not.
		if (!is_keep_alive)
		{
			// Close the connection when the whole content is written out.
			lastContentFuture.addListener(ChannelFutureListener.CLOSE);
		}
	}

	// this method serves as optional overload hook
	public String getContentEncoding()
	{
		return null;
	}

	protected String getFileAbsolutePath(File file)
	{
		String abs_path = file.getAbsolutePath();

		if (!file.getAbsolutePath().startsWith(rootDirectoryPath))
		{
			throw new WebException(new IllegalArgumentException("Forbidden"), HttpResponseStatus.FORBIDDEN.code());
		}

		return abs_path;
	}

	protected File getFile(FullHttpRequest request)
	{
		String path = sanitizePath(request.getUri());

		if (path == null)
		{
			throw new WebException(new IllegalArgumentException("Forbidden"), HttpResponseStatus.FORBIDDEN.code());
		}

		File file = new File(path);
		validateFile(file, request.getUri());
		return file;
	}

	protected void validateFile(File file, String path)
	{
		if (file.isHidden() || !file.exists())
		{
			throw new WebException(new FileNotFoundException(String.format("File not found: '%s'", path)), HttpResponseStatus.NOT_FOUND.code());
		}

		if (!file.isFile())
		{
			throw new WebException(new IllegalArgumentException("Forbidden"), HttpResponseStatus.FORBIDDEN.code());
		}
	}

	protected void validateRequest(FullHttpRequest request)
	{
		if (request.getMethod() != HttpMethod.GET)
		{
			throw new WebException(new IllegalArgumentException("Method not allowed"), HttpResponseStatus.METHOD_NOT_ALLOWED.code());
		}

		if (HttpHeaders.isTransferEncodingChunked(request))
		{
			throw new WebException(new IllegalArgumentException("Bad request"), HttpResponseStatus.BAD_REQUEST.code());
		}
	}

	private String sanitizePath(String spath)
	{
		String path = StringUtils.substringBefore(spath, "?");

		if (StringUtils.isBlank(path))
		{
			return null;
		}

		// Decode the path.
		try
		{
			path = UrlCodec.decode(path, "ISO-8859-1");
		}
		catch (UnsupportedEncodingException e)
		{
			try
			{
				path = UrlCodec.decode(path, "UTF-8");
			}
			catch (UnsupportedEncodingException e1)
			{
				throw new RuntimeException(e1);
			}
		}

		// Convert file separators.
		path = path.replace('/', File.separatorChar);

		// Simplistic dumb security check.
		// You will have to do something serious in the production environment.
		if (path.contains(File.separator + ".") || path.contains("." + File.separator) || path.startsWith(".") || path.endsWith("."))
		{
			return null;
		}
		// Convert to absolute path.

		return rootDirectoryPath + File.separator + path;
	}

	@Override
	protected ResponseFormatter getResponseFormatter()
	{
		return rspFmt;
	}
}