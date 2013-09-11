package org.caudexorigo.jdbc;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

class DbConfigReader
{
	private static final String BUNDLE_NAME = "db"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private DbConfigReader()
	{
	}

	public static String getString(String key)
	{
		try
		{
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}

	public static boolean containsKey(String key)
	{
		return RESOURCE_BUNDLE.containsKey(key);
	}

	public static void validate(String[] args) throws IllegalArgumentException
	{
		String errorMessage = "Essential configuration parameters where not set, check your 'db.properties' file";

		for (int i = 0; i < args.length; i++)
		{
			if (!DbConfigReader.containsKey(args[i]))
			{
				throw new IllegalArgumentException(errorMessage);
			}
		}
	}
}