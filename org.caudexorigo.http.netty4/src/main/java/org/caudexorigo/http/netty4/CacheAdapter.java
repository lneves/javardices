package org.caudexorigo.http.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.ReferenceCountUtil;

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
	private static final CharSequence pass_through = HttpHeaders.newEntity("pass-through");

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
		if (request.getMethod().equals(HttpMethod.GET))
		{
			observeBegin(ctx, request, requestObserver);

			FullHttpResponse response = cachedProcess(ctx, request, requestObserver);

			boolean is_keep_alive = HttpHeaders.isKeepAlive(request);
			commitResponse(ctx, response, is_keep_alive);

			observeEnd(ctx, request, response, requestObserver);
		}
		else
		{
			this.wrapped.process(ctx, request, requestObserver);
		}
	}

	private FullHttpResponse cachedProcess(ChannelHandlerContext ctx, FullHttpRequest request, RequestObserver requestObserver)
	{
		prepareRequest(request);

		final CacheKey ck = cacheKeyBuilder.build(ctx, request);

		FullHttpResponse response = cachedContent.get(ck);

		if ((response == null))
		{
			response = buildResponse(ctx);
			log.debug("Cache miss for: {}", ck);

			ReferenceCountUtil.retain(response);

			try
			{
				wrapped.service(ctx, request, response);
			}
			catch (Throwable t)
			{
				if (response != null)
				{
					ReferenceCountUtil.release(response);
				}
				throw new RuntimeException(t);
			}

			prepareResponse(request, response);

			if (!isCacheable(response))
			{
				log.warn("Response object for resource '{}' is not cacheable.", request.getUri());
				response.headers().set(ncache, pass_through);
				ReferenceCountUtil.release(response);
				return response;
			}

			response.headers().set(ncache, lookup);
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
				log.warn("Empty cache hit for: {}", ck, response.content());
				evict(ck);
				return cachedProcess(ctx, request, requestObserver);
			}
			log.debug("Cache hit for: {}", ck);
			response.headers().set(ncache, hit);

			ReferenceCountUtil.retain(response);
		}

		return response;
	}

	private boolean isCacheable(FullHttpResponse response)
	{
		if (response.content().readableBytes() == 0)
		{
			log.warn("netty bug 'response.content().readableBytes() == 0', pass-through");
			return false;
		}

		if (response.headers().contains(HttpHeaders.Names.SET_COOKIE))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public void prepareRequest(FullHttpRequest request)
	{
		// extension point
	}

	public void prepareResponse(FullHttpRequest request, FullHttpResponse response)
	{
		// extension point
	}

	private void evict(final CacheKey ck)
	{
		log.debug("Evict entry '{}'", ck);
		FullHttpResponse rsp = cachedContent.remove(ck);
		if (rsp != null)
		{
			ReferenceCountUtil.release(rsp);
		}
	}

	public FullHttpResponse removeCachedEntry(CacheKey ck)
	{
		return cachedContent.remove(ck);
	}

	public void clear()
	{
		cachedContent.clear();
	}
}