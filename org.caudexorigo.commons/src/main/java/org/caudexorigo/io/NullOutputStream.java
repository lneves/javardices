package org.caudexorigo.io;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {

  public NullOutputStream() {}

  /**
   * Closes the stream. This method must be called to release any resources associated
   * with the stream.
   * 
   * @exception IOException If an I/O error has occurred.
   */
  public void close() throws IOException {}

  /**
   * Flushes the stream. This will write any buffered output bytes.
   * 
   * @exception IOException If an I/O error has occurred.
   */
  public void flush() throws IOException {}

  /**
   * @param b the data to be written
   * @exception IOException If an I/O error has occurred.
   */
  public void write(byte b[]) throws IOException {}

  /**
   * @param b the data to be written
   * @param off the start offset in the data
   * @param len the number of bytes that are written
   * @exception IOException If an I/O error has occurred.
   */
  public void write(byte b[], int off, int len) throws IOException {}

  /**
   * @param b the byte
   * @exception IOException If an I/O error has occurred.
   */
  public void write(int b) throws IOException {}
}
