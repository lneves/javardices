/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons-sandbox//i18n/src/java/org/apache/commons/i18n/LocalizedException.java,v 1.1 2004/10/04 13:41:09 dflorey Exp $
 * $Revision: 224613 $
 * $Date: 2005-07-24 13:53:05 +0100 (dom, 24 Jul 2005) $
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

import java.text.MessageFormat;
import java.util.Locale;

import org.caudexorigo.i18n.bundles.ErrorBundle;

/**
 * The <code>LocalizedRuntimeException</code> class is the base class for all runtime exceptions that provide locaized error informations.
 * 
 */
public class LocalizedRuntimeException extends RuntimeException
{
	private static final long serialVersionUID = 2760518155404668110L;

	private ErrorBundle errorMessage;

	/**
	 * @param errorMessage
	 *            The error message contains a detailed localized description of the error that caused the exception
	 * @param throwable
	 *            The <code>Throwable</code> that caused this exception
	 */
	public LocalizedRuntimeException(ErrorBundle errorMessage, Throwable throwable)
	{
		super(errorMessage.getSummary(Locale.getDefault(), throwable.getMessage()), throwable);
		this.errorMessage = errorMessage;
	}

	/**
	 * @param errorMessage
	 *            The error message contains a detailed localized description of the error that caused the exception
	 */
	public LocalizedRuntimeException(ErrorBundle errorMessage)
	{
		super(errorMessage.getSummary(Locale.getDefault(), MessageFormat.format(I18nUtils.INTERNAL_MESSAGES.getString(I18nUtils.MESSAGE_ENTRY_NOT_FOUND), new Object[] { errorMessage.getId(), ErrorBundle.SUMMARY })));
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the detailed error message that describes this exception
	 */
	public ErrorBundle getErrorMessage()
	{
		return errorMessage;
	}
}