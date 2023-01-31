package org.caudexorigo.http.netty41;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.text.UrlCodec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.file.Files;
import java.util.Date;
import java.util.Set;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;

public class StaticFileAction extends HttpAction {
  private final File rootDirectory;
  private final String rootDirectoryPath;

  private final long cacheDuration;

  public StaticFileAction(URI rootPath) {
    this(rootPath, 0);
  }

  public StaticFileAction(URI rootPath, long cacheDuration) {
    super();
    this.rootDirectory = new File(rootPath);
    this.rootDirectoryPath = rootDirectory.getAbsolutePath();

    this.cacheDuration = cacheDuration;

    if (!rootDirectory.isDirectory() || !rootDirectory.canRead() || rootDirectory.isHidden()) {
      throw new IllegalArgumentException("Not a valid root directory");
    }
  }

  @Override
  public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse rsp) {
    validateRequest(request);

    File file = getFile(request);

    long clen = file.length();

    HttpResponse response = new DefaultHttpResponse(rsp.protocolVersion(), rsp.status(), false);

    Set<String> previousHeaders = rsp.headers().names();

    for (String hname : previousHeaders) {
      response.headers().add(hname, rsp.headers().get(hname));
    }

    CharSequence ctype = getMimeType(request, file);

    if (StringUtils.isNotBlank(ctype)) {
      response.headers().set(HttpHeaderNames.CONTENT_TYPE, ctype);
    }

    RandomAccessFile raf = null;
    try {
      raf = new RandomAccessFile(file, "r");
    } catch (FileNotFoundException e1) {
      // this exception can be ignored because we already tested for it
    }

    response.setStatus(HttpResponseStatus.OK);
    response.headers().set(HttpHeaderNames.DATE, HttpDateFormat.getCurrentHttpDate());

    if (cacheDuration > 0) {
      response.headers().set(HttpHeaderNames.CACHE_CONTROL, String.format("max-age=%s",
          cacheDuration));
      response.headers().set(HttpHeaderNames.LAST_MODIFIED, HttpDateFormat.getHttpDate(new Date(file
          .lastModified())));
      response.headers().set(HttpHeaderNames.VARY, "Accept-Encoding");
    }
    String contentEncoding = getContentEncoding(request, file);

    if (StringUtils.isNotBlank(contentEncoding)) {
      response.headers().set(HttpHeaderNames.CONTENT_ENCODING, contentEncoding);
    }

    boolean is_keep_alive = HttpUtil.isKeepAlive(request);

    try {
      if (request.method() == HttpMethod.GET) {
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, Long.toString(clen));

        rsp.headers().set(response.headers());
        rsp.setStatus(rsp.status());
        ctx.write(response);

        boolean useSsl = (ctx.pipeline().get(SslHandler.class) != null);

        if (useSsl) {
          is_keep_alive = false;
          ctx.write(new ChunkedFile(raf, 0, clen, 8192 * 2), ctx.voidPromise());
        } else {
          ctx.write(new DefaultFileRegion(raf.getChannel(), 0, clen), ctx.voidPromise());
        }
      } else if (request.method() == HttpMethod.HEAD) {
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, "0");
        rsp.headers().set(response.headers());
        rsp.setStatus(rsp.status());
        ctx.write(response);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Write the end marker
    ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

    // Decide whether to close the connection or not.
    if (!is_keep_alive) {
      // Close the connection when the whole content is written out.
      lastContentFuture.addListener(ChannelFutureListener.CLOSE);
    }
  }

  protected CharSequence getMimeType(HttpRequest request, File file) {
    try {
      // String req_path = StringUtils.substringBefore(request.uri(),
      // "?");
      // String abs_path = getFileAbsolutePath(file);
      String ctype = Files.probeContentType(file.toPath());
      if (StringUtils.isNotBlank(ctype)) {
        return ctype;
      } else {
        return MimeTable.getContentType(file.getPath());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // this method serves as optional overload hook
  public String getContentEncoding(HttpRequest req, File file) {
    return null;
  }

  protected String getFileAbsolutePath(File file) {
    String abs_path = file.getAbsolutePath();

    if (!file.getAbsolutePath().startsWith(rootDirectoryPath)) {
      throw new WebException(new IllegalArgumentException("Forbidden"), HttpResponseStatus.FORBIDDEN
          .code());
    }

    return abs_path;
  }

  protected File getFile(FullHttpRequest request) {
    String req_path = StringUtils.substringBefore(request.uri(), "?");
    return getFile(req_path);
  }

  protected File getFile(String req_path) {
    String fs_path = sanitizePath(req_path);

    if (fs_path == null) {
      throw new WebException(new IllegalArgumentException("Forbidden"), HttpResponseStatus.FORBIDDEN
          .code());
    }

    File file = new File(fs_path);
    validateFile(file, req_path);
    return file;
  }

  protected void validateFile(File file, String path) {
    if (file.isHidden() || !file.exists()) {
      throw new WebException(new FileNotFoundException(String.format("File not found: '%s'", path)),
          HttpResponseStatus.NOT_FOUND.code());
    }

    if (!file.isFile()) {
      throw new WebException(new IllegalArgumentException(), HttpResponseStatus.FORBIDDEN.code());
    }
  }

  protected void validateRequest(FullHttpRequest request) {
    if (!((request.method() == HttpMethod.GET) || (request.method() == HttpMethod.HEAD))) {
      throw new WebException(new IllegalArgumentException("Method not allowed"),
          HttpResponseStatus.METHOD_NOT_ALLOWED.code());
    }

    if (HttpUtil.isTransferEncodingChunked(request)) {
      throw new WebException(new IllegalArgumentException("Bad request"),
          HttpResponseStatus.BAD_REQUEST.code());
    }
  }

  private String sanitizePath(String req_path) {
    if (StringUtils.isBlank(req_path)) {
      return null;
    }

    // Decode the path.
    try {
      req_path = UrlCodec.decode(req_path, "ISO-8859-1");
    } catch (Throwable e) {
      req_path = UrlCodec.decode(req_path, "UTF-8");
    }

    // Convert file separators.
    req_path = req_path.replace('/', File.separatorChar);

    // Simplistic dumb security check.
    // You will have to do something serious in the production environment.
    if (req_path.contains(File.separator + ".") || req_path.contains("." + File.separator)
        || req_path.startsWith(".") || req_path.endsWith(".")) {
      return null;
    }
    // Convert to absolute path.

    return rootDirectoryPath + File.separator + req_path;
  }

  @Override
  protected boolean isZeroCopy() {
    return true;
  }
}
