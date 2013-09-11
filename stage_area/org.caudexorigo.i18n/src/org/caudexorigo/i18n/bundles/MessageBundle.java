/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons-sandbox//i18n/src/java/org/apache/commons/i18n/LocalizedError.java,v 1.1 2004/10/04 13:41:09 dflorey Exp $
 * $Revision: 171112 $
 * $Date: 2005-05-20 13:50:06 +0100 (sex, 20 Mai 2005) $
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
package org.caudexorigo.i18n.bundles;

import java.util.Locale;

import org.caudexorigo.i18n.MessageNotFoundException;

/**
 * <p>
 * The <code>MessageBundle</code> groups together title and text.
 * </p>
 * 
 */
public class MessageBundle extends TextBundle
{

	private static final long serialVersionUID = -2899118117069786798L;

	public final static String TITLE = "title";

	/**
	 * @param messageId
	 *            Unique message id that identifies the message
	 */
	public MessageBundle(String messageId)
	{
		super(messageId);
	}

	/**
	 * @param providerId
	 *            The name of the message provider (i.e. source) to use for the message
	 * @param messageId
	 *            Unique message id that identifies the message
	 */
	public MessageBundle(String providerId, String messageId)
	{
		super(providerId, messageId);
	}

	/**
	 * @param messageId
	 *            Unique message id that identifies the message
	 * @param arguments
	 *            An array of objects conaining the values that should be inserted into the localized message.
	 */
	public MessageBundle(String messageId, Object[] arguments)
	{
		super(messageId, arguments);
	}

	/**
	 * @param providerId
	 *            The name of the message provider (i.e. source) to use for the message
	 * @param messageId
	 *            Unique message id that identifies the message
	 * @param arguments
	 *            An array of objects conaining the values that should be inserted into the localized message.
	 */
	public MessageBundle(String providerId, String messageId, Object[] arguments)
	{
		super(providerId, messageId, arguments);
	}

	/**
	 * @param locale
	 *            The locale that is used to find the appropriate localized text
	 * @return returns the localized message entry with the key <code>title</code>
	 * @throws MessageNotFoundException
	 *             is thrown if no entry with key <code>title</code> could be found in the message bundle identified by the given message identifier
	 */
	public String getTitle(Locale locale) throws MessageNotFoundException
	{
		return getEntry(TITLE, locale);
	}

	/**
	 * @param locale
	 *            The locale that is used to find the appropriate localized text
	 * @param defaultTitle
	 *            The default text will be returned, if no entry with key <code>title</code> could be found in the message bundle identified by the given message identifier
	 * @return returns the localized message entry with the key <code>title</code>
	 */
	public String getTitle(Locale locale, String defaultTitle)
	{
		return getEntry(TITLE, locale, defaultTitle);
	}
}