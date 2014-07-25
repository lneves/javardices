package org.caudexorigo.cli;

/**
 * Thrown when a value has an invalid format
 * 
 * @author Tim Wood
 */
class ValueFormatException extends CliException
{
	private static final long serialVersionUID = -5278572593150819070L;

	/**
	 * A value had an invalid format
	 * 
	 * @param message
	 *            A message describing the problem
	 */
	public ValueFormatException(final String message)
	{
		super(message);
	}
}
