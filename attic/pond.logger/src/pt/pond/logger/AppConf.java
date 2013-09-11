package pt.pond.logger;


import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class AppConf
{
	private static final String BUNDLE_NAME = "conf"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private AppConf()
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
		String errorMessage = "Essential configuration parameters where not set, check your 'conf.properties' file";

		for (int i = 0; i < args.length; i++)
		{
			if (!AppConf.containsKey(args[i]))
			{
				throw new IllegalArgumentException(errorMessage);
			}
		}
	}
}