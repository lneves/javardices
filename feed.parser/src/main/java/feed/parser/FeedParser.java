package feed.parser;

import org.caudexorigo.io.UnsynchronizedByteArrayInputStream;
import org.caudexorigo.io.UnsynchronizedByteArrayOutputStream;
import org.caudexorigo.text.DetectEncoding;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class FeedParser {
  private final StaxParser staxParser;

  public FeedParser() {
    super();
    staxParser = new StaxParser();
  }

  public FeedChannel parse(Reader reader) {
    return staxParser.parse(reader, true, false);
  }

  public FeedChannel parse(Reader reader, boolean failOnError) {
    return staxParser.parse(reader, failOnError, false);
  }

  public FeedChannel parse(Reader reader, boolean failOnError, boolean stripHtml) {
    return staxParser.parse(reader, failOnError, stripHtml);
  }

  public FeedChannel parse(InputStream stream) {
    return parse(stream, true);
  }

  public FeedChannel parse(InputStream stream, boolean failOnError) {
    return parse(stream, failOnError, false);
  }

  public FeedChannel parse(InputStream stream, boolean failOnError, boolean stripHtml) {
    try {
      Reader reader = buildReader(stream);
      return staxParser.parse(reader, failOnError, stripHtml);
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  private Reader buildReader(InputStream stream) throws IOException, UnsupportedEncodingException {
    if (stream.markSupported()) {
      DetectEncoding encDetector = new DetectEncoding("UTF-8");
      stream.mark(512);
      String encoding = encDetector.detect(stream);
      stream.reset();
      encDetector.stripBOM(stream);

      Reader reader = new InputStreamReader(stream, encoding);
      return reader;
    } else {
      UnsynchronizedByteArrayOutputStream bout = new UnsynchronizedByteArrayOutputStream();
      int b;

      while ((b = stream.read()) > -1) {
        bout.write(b);
      }

      Reader r = buildReader(new UnsynchronizedByteArrayInputStream(bout.toByteArray()));
      bout.close();

      return r;
    }
  }
}
