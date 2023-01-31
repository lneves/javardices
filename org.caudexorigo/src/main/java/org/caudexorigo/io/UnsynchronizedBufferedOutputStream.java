package org.caudexorigo.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UnsynchronizedBufferedOutputStream extends FilterOutputStream {
  public UnsynchronizedBufferedOutputStream(OutputStream out) {
    this(out, 8192);
  }

  public UnsynchronizedBufferedOutputStream(OutputStream out, int size) {
    super(out);
    if (size <= 0) {
      throw new IllegalArgumentException("Buffer size <= 0");
    } else {
      buf = new byte[size];
      return;
    }
  }

  protected void flushBuffer() throws IOException {
    if (count > 0) {
      out.write(buf, 0, count);
      count = 0;
    }
  }

  public void write(int b) throws IOException {
    if (count >= buf.length)
      flushBuffer();
    buf[count++] = (byte) b;
  }

  public void write(byte b[], int off, int len) throws IOException {
    if (len >= buf.length) {
      flushBuffer();
      out.write(b, off, len);
      return;
    }
    if (len > buf.length - count)
      flushBuffer();
    System.arraycopy(b, off, buf, count, len);
    count += len;
  }

  public void flush() throws IOException {
    flushBuffer();
    out.flush();
  }

  protected byte buf[];
  protected int count;
}
