/*
 * Copyright 2002-2005 Elliotte Rusty Harold
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of version 2.1 of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 * 
 * You can contact Elliotte Rusty Harold by sending e-mail to elharo@metalab.unc.edu.
 * Please include the word "XOM" in the subject line. The XOM home page is located at
 * http://www.xom.nu/
 */

package org.caudexorigo.nu.xom;

/**
 * <p>
 * Indicates an attempt to create text content that is not allowed in XML 1.0. For
 * example, this could be a text node that contains a vertical tab or an attribute value
 * that contains an unmatched surrogate character.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b3
 *
 */
public class IllegalCharacterDataException extends IllegalDataException {

  private static final long serialVersionUID = 5097647465152879476L;

  /**
   * <p>
   * Creates a new <code>IllegalCharacterDataException</code> with a detail message.
   * </p>
   * 
   * @param message a string indicating the specific problem
   */
  public IllegalCharacterDataException(String message) {
    super(message);
  }

  /**
   * <p>
   * Creates a new <code>IllegalCharacterDataException</code> with a detail message and an
   * underlying root cause.
   * </p>
   * 
   * @param message a string indicating the specific problem
   * @param cause the original cause of this exception
   */
  public IllegalCharacterDataException(String message, Throwable cause) {
    super(message, cause);
  }

}
