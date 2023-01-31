package org.caudexorigo.cli;

/**
 * Superclass of all Cli Exceptions
 * 
 * @author Tim Wood
 */
public class CliException extends Exception {
  private static final long serialVersionUID = 5015614550344133699L;

  /**
   * A new exception with no message
   */
  public CliException() {
    super();
  }

  /**
   * A new exception with the given message and cause
   * 
   * @param message The message
   * @param cause The cause
   */
  public CliException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * A new exception with the given message
   * 
   * @param message The message
   */
  public CliException(final String message) {
    super(message);
  }

  /**
   * A new exception with the given cause
   * 
   * @param cause The cause
   */
  public CliException(final Throwable cause) {
    super(cause);
  }
}
