package org.caudexorigo.http.netty41;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URI;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;

public class GzipStaticFileAction extends StaticFileAction {
  private static final String GZIP_ENCODING = "gzip";

  public GzipStaticFileAction(URI rootPath) {
    super(rootPath);
  }

  public GzipStaticFileAction(URI rootPath, long cacheDuration) {
    super(rootPath, cacheDuration);
  }

  @Override
  protected File getFile(FullHttpRequest request) {
    String original_req_path = StringUtils.substringBefore(request.uri(), "?");
    try {
      if (allowsGzip(request)) {
        String req_path = original_req_path.concat(".gz");
        return getFile(req_path);
      } else {
        return getFile(original_req_path);
      }
    } catch (Throwable t) {
      return getFile(original_req_path);
    }
  }

  @Override
  public String getContentEncoding(HttpRequest request, File file) {
    boolean isGzipFile = file.getPath().endsWith(".gz");
    if (allowsGzip(request) && isGzipFile) {
      return GZIP_ENCODING;
    }
    return null;
  }

  @Override
  protected CharSequence getMimeType(HttpRequest request, File file) {
    String original_req_path = StringUtils.substringBefore(request.uri(), "?");
    return MimeTable.getContentType(original_req_path);
  }

  private boolean allowsGzip(HttpRequest request) {
    String accept_enconding = request.headers().get(HttpHeaderNames.ACCEPT_ENCODING);
    return StringUtils.containsIgnoreCase(accept_enconding, GZIP_ENCODING);
  }
}
