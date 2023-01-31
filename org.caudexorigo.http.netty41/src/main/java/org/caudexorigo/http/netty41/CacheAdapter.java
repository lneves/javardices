package org.caudexorigo.http.netty41;

import org.caudexorigo.concurrent.CustomExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.AsciiString;
import io.netty.util.ReferenceCountUtil;

public class CacheAdapter extends HttpAction {
  private static Logger log = LoggerFactory.getLogger(HttpAction.class);
  private static final ScheduledExecutorService schedExec = CustomExecutors.newScheduledThreadPool(
      2, "sched-exec");
  private static final CharSequence ncache = new AsciiString("X-NCache");
  private static final CharSequence hit = new AsciiString("hit");
  private static final CharSequence lookup = new AsciiString("lookup");
  private static final CharSequence pass_through = new AsciiString("pass-through");

  private HttpAction wrapped;

  private final ConcurrentMap<CacheKey, FullHttpResponse> cachedContent =
      new ConcurrentHashMap<CacheKey, FullHttpResponse>();
  private CacheKeyBuilder cacheKeyBuilder;

  public CacheAdapter(HttpAction wrapped, CacheKeyBuilder cacheKeyBuilder) {
    this.wrapped = wrapped;
    this.cacheKeyBuilder = cacheKeyBuilder;
  }

  @Override
  public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse rsp) {}

  @Override
  protected void process(ChannelHandlerContext ctx, FullHttpRequest request,
      RequestObserver requestObserver) {
    if (request.method().equals(HttpMethod.GET)) {
      observeBegin(ctx, request, requestObserver);

      FullHttpResponse response = cachedProcess(ctx, request, requestObserver);

      boolean is_keep_alive = HttpUtil.isKeepAlive(request);
      commitResponse(ctx, response, is_keep_alive);

      observeEnd(ctx, request, response, requestObserver);
    } else {
      this.wrapped.process(ctx, request, requestObserver);
    }
  }

  private FullHttpResponse cachedProcess(ChannelHandlerContext ctx, FullHttpRequest request,
      RequestObserver requestObserver) {
    prepareRequest(request);

    final CacheKey ck = cacheKeyBuilder.build(ctx, request);

    FullHttpResponse response = cachedContent.get(ck);

    if ((response == null)) {
      response = buildResponse(ctx);
      log.debug("Cache miss for: {}", ck);

      ReferenceCountUtil.retain(response);

      try {
        wrapped.service(ctx, request, response);
      } catch (Throwable t) {
        if (response != null) {
          ReferenceCountUtil.release(response);
        }
        throw new RuntimeException(t);
      }

      prepareResponse(request, response);

      if (!isCacheable(response)) {
        log.warn("Response object for resource '{}' is not cacheable.", request.uri());
        response.headers().set(ncache, pass_through);
        ReferenceCountUtil.release(response);
        return response;
      }

      response.headers().set(ncache, lookup);
      cachedContent.put(ck, response);

      Runnable evictioner = new Runnable() {
        public void run() {
          evict(ck);
        }
      };

      schedExec.schedule(evictioner, ck.getCacheTime(), ck.getCacheTimeUnit());
    } else {
      if (response.content().readableBytes() == 0) {
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

  private boolean isCacheable(FullHttpResponse response) {
    if (response.content().readableBytes() == 0) {
      log.warn("netty bug 'response.content().readableBytes() == 0', pass-through");
      return false;
    }

    if (response.headers().contains(HttpHeaderNames.SET_COOKIE)) {
      return false;
    } else {
      return true;
    }
  }

  public void prepareRequest(FullHttpRequest request) {
    // extension point
  }

  public void prepareResponse(FullHttpRequest request, FullHttpResponse response) {
    // extension point
  }

  private FullHttpResponse evict(final CacheKey ck) {
    log.debug("Evict entry '{}'", ck);
    FullHttpResponse rsp = cachedContent.remove(ck);
    if (rsp != null) {
      ReferenceCountUtil.release(rsp);
    }
    return rsp;
  }

  public FullHttpResponse removeCachedEntry(CacheKey ck) {
    return evict(ck);
  }

  public void clear() {
    Set<CacheKey> keys = new HashSet<CacheKey>();
    keys.addAll(cachedContent.keySet());

    for (CacheKey ck : keys) {
      try {
        evict(ck);
      } catch (Throwable t) {
        log.warn("Error on eviction: {}", t.getMessage());
      }
    }

    cachedContent.clear();
    keys.clear();
  }
}
