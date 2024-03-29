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
 * Indicates an attempt to add a node that already has a parent to the same or a different
 * parent.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b3
 * 
 *
 */
public class MultipleParentException extends IllegalAddException {

  private static final long serialVersionUID = 2605448365902777548L;

  /**
   * <p>
   * Creates a new <code>MultipleParentException</code> with a detail message.
   * </p>
   * 
   * @param message a string indicating the specific problem
   */
  public MultipleParentException(String message) {
    super(message);
  }

  /**
   * <p>
   * Creates a new <code>MultipleParentException</code> with a detail message and an
   * underlying root cause.
   * </p>
   * 
   * @param message a string indicating the specific problem
   * @param cause the original cause of this exception
   */
  public MultipleParentException(String message, Throwable cause) {
    super(message, cause);
  }

}
