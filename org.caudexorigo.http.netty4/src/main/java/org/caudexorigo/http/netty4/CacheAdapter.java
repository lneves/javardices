package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;

import org.caudexorigo.concurrent.CustomExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheAdapter extends HttpAction
{
	private static Logger log = LoggerFactory.getLogger(HttpAction.class);
	private static final ScheduledExecutorService schedExec = CustomExecutors.newScheduledThreadPool(2, "sched-exec");
	private static final CharSequence ncache = HttpHeaders.newEntity("X-NCache");
	private static final CharSequence hit = HttpHeaders.newEntity("hit");
	private static final CharSequence lookup = HttpHeaders.newEntity("lookup");

	private HttpAction wrapped;

	private final ConcurrentMap<CacheKey, FullHttpResponse> cachedContent = new ConcurrentHashMap<CacheKey, FullHttpResponse>();
	private CacheKeyBuilder cacheKeyBuilder;

	public CacheAdapter(HttpAction wrapped, CacheKeyBuilder cacheKeyBuilder)
	{
		this.wrapped = wrapped;
		this.cacheKeyBuilder = cacheKeyBuilder;
	}

	@Override
	public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse rsp)
	{
	}

	@Override
	protected void process(ChannelHandlerContext ctx, FullHttpRequest request, RequestObserver requestObserver)
	{
		observeBegin(ctx, request, requestObserver);

		FullHttpResponse response = cachedProcess(ctx, request, requestObserver);

		observeEnd(ctx, request, response, requestObserver);
	}

	private FullHttpResponse cachedProcess(ChannelHandlerContext ctx, FullHttpRequest request, RequestObserver requestObserver)
	{
		final CacheKey ck = cacheKeyBuilder.build(ctx, request);

		FullHttpResponse response = cachedContent.get(ck);

		if ((response == null))
		{
			response = buildResponse(ctx);
			log.debug("Cache miss for: {}", ck);
			response.headers().set(ncache, lookup);

			response.retain();
			wrappedProcess(ctx, request, response, requestObserver);

			cachedContent.put(ck, response);

			Runnable evictioner = new Runnable()
			{
				public void run()
				{
					evict(ck);
				}
			};

			schedExec.schedule(evictioner, ck.getCacheTime(), ck.getCacheTimeUnit());
		}
		else
		{
			if (response.content().readableBytes() == 0)
			{
				log.warn("Empty cache hit for: {}, readable bytes: {}", ck, response.content().readableBytes());
				evict(ck);
				return cachedProcess(ctx, request, requestObserver);
			}
			log.debug("Cache hit for: {}", ck);
			response.headers().set(ncache, hit);

			response.retain();
			doProcess(ctx, request, response);
		}

		return response;
	}

	private void evict(final CacheKey ck)
	{
		log.debug("Evict entry '{}'", ck);
		FullHttpResponse rsp = cachedContent.remove(ck);
		if (rsp != null)
		{
			rsp.release();
		}
	}

	public FullHttpResponse removeCachedEntry(CacheKey ck)
	{
		return cachedContent.remove(ck);
	}

	private void wrappedProcess(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response, RequestObserver requestObserver)
	{
		boolean is_keep_alive = HttpHeaders.isKeepAlive(request);
		try
		{
			wrapped.service(ctx, request, response);
		}
		catch (Throwable ex)
		{
			handleError(ctx, request, ex, requestObserver);
		}
		finally
		{
			commitResponse(ctx, response, is_keep_alive);
		}
	}

	public void clear()
	{
		cachedContent.clear();
	}
}
