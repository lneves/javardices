package org.caudexorigo.http.netty4;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.text.UrlCodec;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

public class NettyRequest {
  private static final String DEFAULT_CHARSET_NAME = "ISO-8859-1";
  private static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
  private static final String UTF8_CHARSET_NAME = "UTF-8";
  private static final Charset UTF8_CHARSET = Charset.forName(UTF8_CHARSET_NAME);

  private static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
  private static final String X_WWW_FORM_URLENCODED_UTF8 =
      "application/x-www-form-urlencoded; charset=UTF-8";

  private Charset charset;
  private String charsetName;

  private Map<String, List<String>> parameters;
  private String path;
  private String queryString;
  private FullHttpRequest request;

  public NettyRequest(FullHttpRequest request) {
    this(request, null);
  }

  public NettyRequest(FullHttpRequest request, Charset charset) {
    super();
    this.request = request;

    if (charset != null) {
      this.charset = charset;
    } else {
      this.charset = DEFAULT_CHARSET;
    }

    this.charsetName = this.charset.displayName();
  }

  private void initParameters() {
    if (parameters == null) {
      parameters = new HashMap<String, List<String>>();

      setParameters(getQueryString());

      String content_type = request.headers().get(HttpHeaders.Names.CONTENT_TYPE);

      if (request.getMethod().equals(HttpMethod.POST) && (StringUtils.contains(content_type,
          X_WWW_FORM_URLENCODED)) && (HttpHeaders.getContentLength(request) > 0)) {
        if (StringUtils.contains(content_type, UTF8_CHARSET_NAME)) {
          charset = UTF8_CHARSET;
        }
        setParameters(request.content().toString(charset));
      }
    }
  }

  public String getCharacterEncoding() {
    return this.charsetName;
  }

  public void setCharacterEncoding(String encoding) {
    this.charset = Charset.forName(encoding);
    this.charsetName = charset.displayName();
  }

  public void addHeader(String name, Object value) {
    request.headers().set(name, value);
  }

  public void addParameter(String name, String value) {
    initParameters();

    List<String> values = parameters.get(name);
    if (values == null) {
      values = new ArrayList<String>();
      parameters.put(name, values);
    }
    values.add(value);
  }

  public void clearHeaders() {
    request.headers().clear();
  }

  public boolean containsHeader(String name) {
    return request.headers().contains(name);
  }

  public ByteBuf getContent() {
    return request.content();
  }

  public long getContentLength() {
    String contentLength = getHeader(Names.CONTENT_LENGTH.toString());
    if (contentLength != null) {
      return Long.parseLong(contentLength);
    }

    return 0l;
  }

  public String getHeader(String name) {
    return request.headers().get(name);
  }

  public Set<String> getHeaderNames() {
    return request.headers().names();
  }

  public List<Entry<String, String>> getHeaders() {
    return request.headers().entries();
  }

  public List<String> getHeaders(String name) {
    return request.headers().getAll(name);
  }

  public HttpMethod getMethod() {
    return request.getMethod();
  }

  public String getParameter(String name) {
    initParameters();

    List<String> values = parameters.get(name);
    if (values == null) {
      return null;
    }

    return values.get(0);
  }

  public Map<String, List<String>> getParameters() {
    initParameters();
    return Collections.unmodifiableMap(parameters);
  }

  public List<String> getParameters(String name) {
    initParameters();

    List<String> values = parameters.get(name);
    if (values == null) {
      return null;
    }

    return values;
  }

  public String getPath() {
    if (path == null) {
      this.path = StringUtils.substringBefore(request.getUri(), "?");
    }
    return path;
  }

  public HttpVersion getProtocolVersion() {
    return request.getProtocolVersion();
  }

  public String getQueryString() {
    if (queryString == null) {
      this.queryString = StringUtils.substringAfter(request.getUri(), "?");
    }
    return queryString;
  }

  public String getUri() {
    return request.getUri();
  }

  public boolean isChunked() {
    return HttpHeaders.isTransferEncodingChunked(request);
  }

  public boolean isKeepAlive() {
    return HttpHeaders.isKeepAlive(request);
  }

  public void removeHeader(String name) {
    request.headers().remove(name);
  }

  // public void setChunked(boolean ischunked)
  // {
  // request.setChunked(ischunked);
  // }

  public void setHeader(String name, Iterable<?> values) {
    request.headers().set(name, values);

  }

  public void setHeader(String name, Object value) {
    request.headers().set(name, value);
  }

  public void setMethod(HttpMethod httpMethod) {
    request.setMethod(httpMethod);
  }

  private void setParameters(String parameters) {
    try {
      this.setParameters(parameters, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new InternalError(charsetName + " decoder must be provided by JDK.");
    }
  }

  private void setParameters(String parameters, String encoding)
      throws UnsupportedEncodingException {

    if (StringUtils.isBlank(parameters)) {
      return;
    }

    // clearParameters();

    int pos = 0;
    while (pos < parameters.length()) {
      int ampPos = parameters.indexOf('&', pos);

      String value;
      if (ampPos < 0) {
        value = parameters.substring(pos);
        ampPos = parameters.length();
      } else {
        value = parameters.substring(pos, ampPos);
      }

      int equalPos = value.indexOf('=');
      if (equalPos < 0) {
        this.addParameter(UrlCodec.decode(value, encoding), "");
      } else {
        this.addParameter(UrlCodec.decode(value.substring(0, equalPos), encoding), UrlCodec.decode(
            value.substring(equalPos + 1), encoding));
      }

      pos = ampPos + 1;
    }
  }

  public void setProtocolVersion(HttpVersion hversion) {
    request.setProtocolVersion(hversion);
  }

  public void setUri(String uri) {
    request.setUri(uri);
  }
}
