package org.caudexorigo.http.netty4;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import java.io.File;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.http.netty4.reporting.ResponseFormatter;
import org.caudexorigo.http.netty4.reporting.StandardResponseFormatter;

public class GzipStaticFileAction extends StaticFileAction
{
	private static final String GZIP_ENCODING = "gzip";

	public GzipStaticFileAction(URI rootPath)
	{
		super(rootPath);
	}

	public GzipStaticFileAction(URI rootPath, long cacheAge)
	{
		super(rootPath, new StandardResponseFormatter(false), cacheAge);
	}

	public GzipStaticFileAction(URI rootPath, ResponseFormatter rspFmt, long cacheAge)
	{
		super(rootPath, rspFmt, cacheAge);
	}

	public GzipStaticFileAction(URI rootPath, ResponseFormatter rspFmt)
	{
		super(rootPath, rspFmt);
	}

	@Override
	protected File getFile(FullHttpRequest request)
	{
		String original_req_path = StringUtils.substringBefore(request.getUri(), "?");
		try
		{
			if (allowsGzip(request))
			{
				String req_path = original_req_path.concat(".gz");
				return getFile(req_path);
			}
			else
			{
				return getFile(original_req_path);
			}
		}
		catch (Throwable t)
		{
			return getFile(original_req_path);
		}
	}

	@Override
	public String getContentEncoding(HttpRequest request, File file)
	{
		boolean isGzipFile = file.getPath().endsWith(".gz");
		if (allowsGzip(request) && isGzipFile)
		{
			return GZIP_ENCODING;
		}
		return null;
	}

	private boolean allowsGzip(HttpRequest request)
	{
		String accept_enconding = request.headers().get(HttpHeaders.Names.ACCEPT_ENCODING);
		return StringUtils.containsIgnoreCase(accept_enconding, GZIP_ENCODING);
	}
}