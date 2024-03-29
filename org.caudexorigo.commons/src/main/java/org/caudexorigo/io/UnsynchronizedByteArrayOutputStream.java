package org.caudexorigo.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements an output stream in which the data is written into a byte array.
 * The buffer automatically grows as data is written to it.
 * <p>
 * The data can be retrieved using <code>toByteArray()</code> and <code>toString()</code>.
 * <p>
 * Closing a <tt>ByteArrayOutputStream</tt> has no effect. The methods in this class can
 * be called after the stream has been closed without generating an <tt>IOException</tt>.
 * <p>
 * This is an alternative implementation of the java.io.ByteArrayOutputStream class. The
 * original implementation only allocates 32 bytes at the beginning. As this class is
 * designed for heavy duty it starts at 1024 bytes. In contrast to the original it doesn't
 * reallocate the whole memory block but allocates additional buffers. This way no buffers
 * need to be garbage collected and the contents don't have to be copied to the new
 * buffer. This class is designed to behave exactly like the original. The only exception
 * is the deprecated toString(int) method that has been ignored.
 * 
 */
public class UnsynchronizedByteArrayOutputStream extends OutputStream {

  /** A singleton empty byte array. */
  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

  /** The list of buffers, which grows and never reduces. */
  private List<byte[]> buffers = new ArrayList<byte[]>();

  /** The index of the current buffer. */
  private int currentBufferIndex;

  /** The total count of bytes in all the filled buffers. */
  private int filledBufferSum;

  /** The current buffer. */
  private byte[] currentBuffer;

  /** The total count of bytes written. */
  private int count;

  /**
   * Creates a new byte array output stream. The buffer capacity is initially 1024 bytes,
   * though its size increases if necessary.
   */
  public UnsynchronizedByteArrayOutputStream() {
    this(1024);
  }

  /**
   * Creates a new byte array output stream, with a buffer capacity of the specified size,
   * in bytes.
   * 
   * @param size the initial size
   * @throws IllegalArgumentException if size is negative
   */
  public UnsynchronizedByteArrayOutputStream(int size) {
    if (size < 0) {
      throw new IllegalArgumentException("Negative initial size: " + size);
    }
    needNewBuffer(size);
  }

  /**
   * Return the appropriate <code>byte[]</code> buffer specified by index.
   * 
   * @param index the index of the buffer required
   * @return the buffer
   */
  private byte[] getBuffer(int index) {
    return buffers.get(index);
  }

  /**
   * Makes a new buffer available either by allocating a new one or re-cycling an existing
   * one.
   * 
   * @param newcount the size of the buffer if one is created
   */
  private void needNewBuffer(int newcount) {
    if (currentBufferIndex < buffers.size() - 1) {
      // Recycling old buffer
      filledBufferSum += currentBuffer.length;

      currentBufferIndex++;
      currentBuffer = getBuffer(currentBufferIndex);
    } else {
      // Creating new buffer
      int newBufferSize;
      if (currentBuffer == null) {
        newBufferSize = newcount;
        filledBufferSum = 0;
      } else {
        newBufferSize = Math.max(currentBuffer.length << 1, newcount - filledBufferSum);
        filledBufferSum += currentBuffer.length;
      }

      currentBufferIndex++;
      currentBuffer = new byte[newBufferSize];
      buffers.add(currentBuffer);
    }
  }

  /**
   * @see java.io.OutputStream#write(byte[], int, int)
   */
  public void write(byte[] b, int off, int len) {
    if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off
        + len) < 0)) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return;
    }

    int newcount = count + len;
    int remaining = len;
    int inBufferPos = count - filledBufferSum;
    while (remaining > 0) {
      int part = Math.min(remaining, currentBuffer.length - inBufferPos);
      System.arraycopy(b, off + len - remaining, currentBuffer, inBufferPos, part);
      remaining -= part;
      if (remaining > 0) {
        needNewBuffer(newcount);
        inBufferPos = 0;
      }
    }
    count = newcount;

  }

  /**
   * @see java.io.OutputStream#write(int)
   */
  public synchronized void write(int b) {
    int inBufferPos = count - filledBufferSum;
    if (inBufferPos == currentBuffer.length) {
      needNewBuffer(count + 1);
      inBufferPos = 0;
    }
    currentBuffer[inBufferPos] = (byte) b;
    count++;
  }

  /**
   * @see java.io.ByteArrayOutputStream#size()
   */
  public synchronized int size() {
    return count;
  }

  /**
   * Closing a <tt>ByteArrayOutputStream</tt> has no effect. The methods in this class can
   * be called after the stream has been closed without generating an
   * <tt>IOException</tt>.
   * 
   * @throws IOException never (this method should not declare this exception but it has
   *         to now due to backwards compatability)
   */
  public void close() throws IOException {
    // nop
  }

  /**
   * @see java.io.ByteArrayOutputStream#reset()
   */
  public void reset() {
    count = 0;
    filledBufferSum = 0;
    currentBufferIndex = 0;
    currentBuffer = getBuffer(currentBufferIndex);
  }

  /**
   * Writes the entire contents of this byte stream to the specified output stream.
   * 
   * @param out the output stream to write to
   * @throws IOException if an I/O error occurs, such as if the stream is closed
   * @see java.io.ByteArrayOutputStream#writeTo(OutputStream)
   */
  public void writeTo(OutputStream out) throws IOException {
    int remaining = count;
    for (int i = 0; i < buffers.size(); i++) {
      byte[] buf = getBuffer(i);
      int c = Math.min(buf.length, remaining);
      out.write(buf, 0, c);
      remaining -= c;
      if (remaining == 0) {
        break;
      }
    }
  }

  /**
   * Gets the curent contents of this byte stream as a byte array. The result is
   * independent of this stream.
   * 
   * @return the current contents of this output stream, as a byte array
   * @see java.io.ByteArrayOutputStream#toByteArray()
   */
  public byte[] toByteArray() {
    int remaining = count;
    if (remaining == 0) {
      return EMPTY_BYTE_ARRAY;
    }
    byte newbuf[] = new byte[remaining];
    int pos = 0;
    for (int i = 0; i < buffers.size(); i++) {
      byte[] buf = getBuffer(i);
      int c = Math.min(buf.length, remaining);
      System.arraycopy(buf, 0, newbuf, pos, c);
      pos += c;
      remaining -= c;
      if (remaining == 0) {
        break;
      }
    }
    return newbuf;
  }

  /**
   * Gets the curent contents of this byte stream as a string.
   * 
   * @see java.io.ByteArrayOutputStream#toString()
   */
  public String toString() {
    return new String(toByteArray());
  }

  /**
   * Gets the curent contents of this byte stream as a string using the specified
   * encoding.
   * 
   * @param enc the name of the character encoding
   * @return the string converted from the byte array
   * @throws UnsupportedEncodingException if the encoding is not supported
   * @see java.io.ByteArrayOutputStream#toString(String)
   */
  public String toString(String enc) throws UnsupportedEncodingException {
    return new String(toByteArray(), enc);
  }

}
