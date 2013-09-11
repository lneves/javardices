package org.caudexorigo.jdbc;

import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;

import org.caudexorigo.Shutdown;

class DbConfigReader
{
	private Properties props;
	private final String resourcePath;

	DbConfigReader(Properties props)
	{
		this.resourcePath = "(void)";
		this.props = props;
	}

	DbConfigReader(String resourcePath)
	{
		this.resourcePath = resourcePath;

		InputStream in_stream = null;
		try
		{
			try
			{
				in_stream = DbConfigReader.class.getResourceAsStream(resourcePath);

				this.props = new Properties();
				this.props.load(in_stream);
			}
			finally
			{
				if (in_stream != null)
				{
					in_stream.close();
				}
			}
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}

	public String getString(String key)
	{
		try
		{
			return props.getProperty(key);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}

	private boolean containsKey(String key)
	{
		return props.containsKey(key);
	}

	public void validate(String[] args) throws IllegalArgumentException
	{
		String errorMessage = String.format("Essential configuration parameters where not set, check your '%s' file", resourcePath);

		for (int i = 0; i < args.length; i++)
		{
			if (!containsKey(args[i]))
			{
				throw new IllegalArgumentException(errorMessage);
			}
		}
	}

	@Override
	public String toString()
	{
		return String.format("DbConfigReader [resourcePath=%s, props=%s]", resourcePath, props);
	}

	public String getPropsAsString()
	{
		return props.toString();
	}
}