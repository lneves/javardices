package org.caudexorigo.http.netty41;

import io.netty.handler.codec.http.HttpHeaders;

public class CachedResponse {
  private final HttpHeaders headers;
  private final byte[] body;

  public CachedResponse(HttpHeaders headers, byte[] body) {
    super();
    this.headers = headers;
    this.body = body;
  }

  public HttpHeaders headers() {
    return headers;
  }

  public byte[] body() {
    return body;
  }
}
