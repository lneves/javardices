/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons-sandbox//i18n/src/java/org/apache/commons/i18n/MessageNotFoundException.java,v 1.1 2004/10/04 13:41:09 dflorey Exp $
 * $Revision: 155449 $
 * $Date: 2005-02-26 13:22:13 +0000 (sáb, 26 Fev 2005) $
 *
 * ====================================================================
 *
 * Copyright 2004 The Apache Software Foundation 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.caudexorigo.i18n;

/**
 * The <code>MessageNotFoundException</code> indicates that a particular message could not be found by using the given message id.
 * 
 */
public class MessageNotFoundException extends RuntimeException
{

	private static final long serialVersionUID = 5272900936237136431L;

	/**
	 * Constructs a new runtime exception with the specified detail message indicating that a particular message could not be found. The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
	 */
	public MessageNotFoundException(String message)
	{
		super(message);
	}

	/**
	 * Constructs a new runtime exception with the specified detail message indicating that a particular message and cause indicating that a particular message could not be found.
	 * <p>
	 * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this runtime exception's detail message.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public MessageNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
