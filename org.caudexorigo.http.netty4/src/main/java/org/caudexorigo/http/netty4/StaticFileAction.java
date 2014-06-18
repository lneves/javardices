package org.caudexorigo.http.netty4;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Date;

import org.caudexorigo.text.StringUtils;
import org.caudexorigo.text.UrlCodec;

public class StaticFileAction extends HttpAction
{
	private final File rootDirectory;
	private final String rootDirectoryPath;

	private final long cacheAge;
	private boolean useSsl;

	public StaticFileAction(URI root_path, boolean useSsl)
	{
		this(root_path, useSsl, 0);
	}

	public StaticFileAction(URI root_path, boolean useSsl, long cache_age)
	{
		this.useSsl = useSsl;
		rootDirectory = new File(root_path);
		rootDirectoryPath = rootDirectory.getAbsolutePath();

		this.cacheAge = cache_age;

		if (!rootDirectory.isDirectory() || !rootDirectory.canRead() || rootDirectory.isHidden())
		{
			throw new IllegalArgumentException("Not a valid root directory");
		}
	}

	@Override
	public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		validateRequest(request);
		File file = getFile(request);

		String abs_path = getFileAbsolutePath(file);

		long clen = file.length();

		System.out.println("StaticFileAction.service.abs_path: " + abs_path);
		System.out.println("StaticFileAction.service.clen1: " + clen);

		response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, Long.toString(clen));

		String ctype = MimeTable.getContentType(abs_path);
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

		if (is_keep_alive)
		{
			response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}

		ctx.write(response);

		ChannelFuture sendFileFuture;
		try
		{
			if (useSsl)
			{
				is_keep_alive = false;
				sendFileFuture = ctx.write(new ChunkedFile(raf, 0, clen, 8192*2), ctx.newProgressivePromise());
			}
			else
			{
				System.out.println("StaticFileAction.service(no ssl)");
				sendFileFuture = ctx.write(new DefaultFileRegion(raf.getChannel(), 0, clen), ctx.newProgressivePromise());
				//sendFileFuture = ctx.write(new ChunkedFile(raf, 0, clen, 8192*2), ctx.newProgressivePromise());
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		sendFileFuture.addListener(new ChannelProgressiveFutureListener()
		{
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total)
			{
				if (total < 0)
				{ // total unknown
					System.out.println(future.channel() + " Transfer progress: " + progress);
				}
				else
				{
					System.out.println(future.channel() + " Transfer progress: " + progress + " / " + total);
				}
			}

			public void operationComplete(ChannelProgressiveFuture future)
			{
				System.out.println(future.channel() + " Transfer complete.");
			}
		});

		// Write the end marker
		ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

		// Decide whether to close the connection or not.
		if (!is_keep_alive)
		{
			// Close the connection when the whole content is written out.
			lastContentFuture.addListener(ChannelFutureListener.CLOSE);
		}
		else
		{
			lastContentFuture.addListener(new ChannelFutureListener()
			{
				@Override
				public void operationComplete(ChannelFuture future) throws Exception
				{
					System.out.println(future.channel() + " Transfer End.");
				}
			});
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
}