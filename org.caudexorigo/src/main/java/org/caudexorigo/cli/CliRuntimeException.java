/**
 *
 */
package org.caudexorigo.cli;

/**
 * The superclass of all Cli Runtime Exception
 * 
 * @author tim
 */
public class CliRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 6028801435353855311L;

  /**
   * A new exception with no message
   */
  public CliRuntimeException() {
    super();
  }

  /**
   * A new exception with the given message and cause
   * 
   * @param message The message
   * @param cause The cause
   */
  public CliRuntimeException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * A new exception with the given message
   * 
   * @param message The message
   */
  public CliRuntimeException(final String message) {
    super(message);
  }

  /**
   * A new exception with the given cause
   * 
   * @param cause The cause
   */
  public CliRuntimeException(final Throwable cause) {
    super(cause);
  }
}
