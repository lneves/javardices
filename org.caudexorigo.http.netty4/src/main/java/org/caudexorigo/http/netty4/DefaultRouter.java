package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

public class DefaultRouter implements RequestRouter
{
	private final HttpAction hello = new HelloWorldAction();
	private final HttpAction gz_hello = new CompressionAdapter(hello);
	private final HttpAction gz_cached_hello = new CompressionAdapter(gz_hello);

	private final CacheKeyBuilder cacheKeyBuilder = new CacheKeyBuilder()
	{
		private static final String GZIP_ENCODING = "gzip";
		private static final String DEFLATE_ENCODING = "deflate";

		@Override
		public CacheKey build(ChannelHandlerContext ctx, FullHttpRequest request)
		{
			String path = StringUtils.substringBefore(request.getUri(), "?");

			String accept_enconding = StringUtils.trimToEmpty(request.headers().get(HttpHeaders.Names.ACCEPT_ENCODING));
			boolean allows_gzip = StringUtils.containsIgnoreCase(accept_enconding, GZIP_ENCODING);
			boolean allows_deflate = StringUtils.containsIgnoreCase(accept_enconding, DEFLATE_ENCODING);

			final String content_encoding;

			if (allows_gzip)
			{
				content_encoding = GZIP_ENCODING;
			}
			else if (allows_deflate)
			{
				content_encoding = DEFLATE_ENCODING;
			}
			else
			{
				content_encoding = "plain";
			}

			return new CacheKey(60l, TimeUnit.MINUTES, path, content_encoding);
		}
	};

	private final HttpAction cached_hello = new CacheAdapter(hello, cacheKeyBuilder);

	@Override
	public HttpAction map(ChannelHandlerContext ctx, FullHttpRequest req)
	{
		if ("/hello".equals(req.getUri()))
		{
			return hello;
		}
		else if ("/gz_hello".equals(req.getUri()))
		{
			return gz_hello;
		}
		else if ("/gz_cached_hello".equals(req.getUri()))
		{
			return gz_cached_hello;
		}
		else if ("/cached_hello".equals(req.getUri()))
		{
			return cached_hello;
		}
		return null;
	}
}