package org.glassfish.jersey.jdkhttp.internal;

import org.glassfish.jersey.internal.l10n.Localizable;
import org.glassfish.jersey.internal.l10n.LocalizableMessageFactory;
import org.glassfish.jersey.internal.l10n.Localizer;

/**
 * Defines string formatting method for each constant in the resource file
 * 
 */
public final class LocalizationMessages
{

	private final static LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("org.glassfish.jersey.jdkhttp.internal.localization");
	private final static Localizer localizer = new Localizer();

	public static Localizable localizableERROR_CONTAINER_URI_SCHEME_UNKNOWN(Object arg0)
	{
		return messageFactory.getMessage("error.container.uri.scheme.unknown", arg0);
	}

	/**
	 * The URI scheme, of the URI {0} must be equal (ignoring case) to 'http' or 'https'.
	 * 
	 */
	public static String ERROR_CONTAINER_URI_SCHEME_UNKNOWN(Object arg0)
	{
		return localizer.localize(localizableERROR_CONTAINER_URI_SCHEME_UNKNOWN(arg0));
	}

	public static Localizable localizableERROR_RESPONSEWRITER_RESPONSE_UNCOMMITED()
	{
		return messageFactory.getMessage("error.responsewriter.response.uncommited");
	}

	/**
	 * ResponseWriter was not commited yet. Committing the Response now.
	 * 
	 */
	public static String ERROR_RESPONSEWRITER_RESPONSE_UNCOMMITED()
	{
		return localizer.localize(localizableERROR_RESPONSEWRITER_RESPONSE_UNCOMMITED());
	}

	public static Localizable localizableERROR_CONTAINER_URI_PATH_EMPTY(Object arg0)
	{
		return messageFactory.getMessage("error.container.uri.path.empty", arg0);
	}

	/**
	 * The URI path, of the URI {0} must be present (not an empty string).
	 * 
	 */
	public static String ERROR_CONTAINER_URI_PATH_EMPTY(Object arg0)
	{
		return localizer.localize(localizableERROR_CONTAINER_URI_PATH_EMPTY(arg0));
	}

	public static Localizable localizableERROR_CONTAINER_URI_NULL()
	{
		return messageFactory.getMessage("error.container.uri.null");
	}

	/**
	 * The URI must not be null.
	 * 
	 */
	public static String ERROR_CONTAINER_URI_NULL()
	{
		return localizer.localize(localizableERROR_CONTAINER_URI_NULL());
	}

	public static Localizable localizableERROR_CONTAINER_URI_PATH_START(Object arg0)
	{
		return messageFactory.getMessage("error.container.uri.path.start", arg0);
	}

	/**
	 * The URI path, of the URI {0} must start with a '/'.
	 * 
	 */
	public static String ERROR_CONTAINER_URI_PATH_START(Object arg0)
	{
		return localizer.localize(localizableERROR_CONTAINER_URI_PATH_START(arg0));
	}

	public static Localizable localizableERROR_CONTAINER_EXCEPTION_IO()
	{
		return messageFactory.getMessage("error.container.exception.io");
	}

	/**
	 * IOException thrown when creating the JDK HttpServer.
	 * 
	 */
	public static String ERROR_CONTAINER_EXCEPTION_IO()
	{
		return localizer.localize(localizableERROR_CONTAINER_EXCEPTION_IO());
	}

	public static Localizable localizableERROR_CONTAINER_URI_PATH_NULL(Object arg0)
	{
		return messageFactory.getMessage("error.container.uri.path.null", arg0);
	}

	/**
	 * The URI path, of the URI {0} must be non-null.
	 * 
	 */
	public static String ERROR_CONTAINER_URI_PATH_NULL(Object arg0)
	{
		return localizer.localize(localizableERROR_CONTAINER_URI_PATH_NULL(arg0));
	}

}
