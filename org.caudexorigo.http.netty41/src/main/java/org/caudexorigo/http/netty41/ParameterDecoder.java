package org.caudexorigo.http.netty41;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.text.UrlCodec;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;

public class ParameterDecoder {
  private static final String DEFAULT_CHARSET_NAME = "ISO-8859-1";
  private static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
  private static final String UTF8_CHARSET_NAME = "UTF-8";
  private static final Charset UTF8_CHARSET = Charset.forName(UTF8_CHARSET_NAME);
  private static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
  private static final Pattern splitter = Pattern.compile(",|;");

  private Charset charset;
  private String charsetName;

  private Map<String, List<String>> parameters;
  private String path;
  private String queryString;
  private FullHttpRequest request;

  public ParameterDecoder(FullHttpRequest request) {
    this(request, null);
  }

  public ParameterDecoder(FullHttpRequest request, Charset charset) {
    super();
    this.request = request;

    if (charset != null) {
      this.charset = charset;
    } else {
      this.charset = DEFAULT_CHARSET;
    }

    this.charsetName = this.charset.displayName();
  }

  private void addParameter(String name, String value) {
    initParameters();

    List<String> values = parameters.get(name);
    if (values == null) {
      values = new ArrayList<String>();
      parameters.put(name, values);
    }
    values.add(value);
  }

  public String coalesce(String... values) {
    for (String v : values) {
      if (StringUtils.isNotBlank(v)) {
        return v;
      }
    }
    return null;
  }

  public String getCharacterEncoding() {
    return this.charsetName;
  }

  public List<String> getMultiParameters(ParameterDecoder pdec, String... param_names) {
    List<String> lst_target = new ArrayList<String>();

    for (String param_name : param_names) {
      List<String> tmp_lst = pdec.getParameters(param_name);

      if (tmp_lst != null) {
        for (String tmp_s : tmp_lst) {
          String[] tmp_arr_s = splitter.split(tmp_s);

          for (String ts : tmp_arr_s) {
            String g = StringUtils.trimToNull(ts);

            if (g != null) {
              lst_target.add(g);
            }
          }
        }
      }
    }

    return lst_target;
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
      this.path = StringUtils.substringBefore(request.uri(), "?");
    }
    return path;
  }

  public String getQueryString() {
    if (queryString == null) {
      this.queryString = StringUtils.substringAfter(request.uri(), "?");
    }
    return queryString;
  }

  public String getUri() {
    return request.uri();
  }

  private void initParameters() {
    if (parameters == null) {
      parameters = new HashMap<String, List<String>>();
      setParameters(getQueryString());

      // parameters = (new QueryStringDecoder(request.uri(), charset,
      // true)).getParameters();

      String content_type = request.headers().get(HttpHeaderNames.CONTENT_TYPE);

      if (request.method().equals(HttpMethod.POST) && (StringUtils.contains(content_type,
          X_WWW_FORM_URLENCODED)) && (HttpUtil.getContentLength(request) > 0)) {
        if (StringUtils.contains(content_type, UTF8_CHARSET_NAME)) {
          charset = UTF8_CHARSET;
        }
        setParameters(request.content().toString(charset));

        // parameters.putAll((new
        // QueryStringDecoder(request.getContent().toString(charset), charset,
        // false)).getParameters());
      }
    }
  }

  public void setCharacterEncoding(String encoding) {
    this.charset = Charset.forName(encoding);
    this.charsetName = charset.displayName();
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

  public boolean tryParse(String parameter, boolean default_value) {
    try {
      if (StringUtils.isBlank(parameter)) {
        return default_value;
      } else {
        return Boolean.parseBoolean(parameter);
      }
    } catch (Throwable e) {
      return default_value;
    }
  }

  public double tryParse(String parameter, double default_value) {
    try {
      if (StringUtils.isBlank(parameter)) {
        return default_value;
      } else {
        return Double.parseDouble(parameter);
      }
    } catch (Throwable e) {
      return default_value;
    }
  }

  public int tryParse(String parameter, int default_value) {
    try {
      if (StringUtils.isBlank(parameter)) {
        return default_value;
      } else {
        return Integer.parseInt(parameter);
      }
    } catch (Throwable e) {
      return default_value;
    }
  }

  public long tryParse(String parameter, long default_value) {
    try {
      if (StringUtils.isBlank(parameter)) {
        return default_value;
      } else {
        return Long.parseLong(parameter);
      }
    } catch (Throwable e) {
      return default_value;
    }
  }
}
