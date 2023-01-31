package org.caudexorigo.jpt;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JptInstanceBuilder
{
	private static final Logger log = LoggerFactory.getLogger(JptInstanceBuilder.class);

	// key: templatePath
	private final Cache<URI, JptInstance> template_cache = new Cache<URI, JptInstance>();

	private static final JptInstanceBuilder instance = new JptInstanceBuilder();

	private JptInstanceBuilder()
	{
	}

	public static JptInstance getJptInstance(final URI templateUri)
	{
		return getJptInstance(JptConfiguration.DEFAULT_CONFIG, templateUri);
	}

	public static JptInstance getJptInstance(final JptConfiguration jptConf, final URI templateUri)
	{
		try
		{
			final CacheFiller<URI, JptInstance> template_cache_listeners_cf = new CacheFiller<URI, JptInstance>()
			{
				public JptInstance populate(URI templateUri)
				{
					return new JptInstance(jptConf, templateUri);
				}
			};

			JptInstance t = instance.template_cache.get(templateUri, template_cache_listeners_cf);
			boolean isStale = isStale(jptConf.checkModified(), t);
			if (isStale)
			{
				// TODO: check the effect of multiple threads hitting this
				instance.template_cache.remove(templateUri);
				t = instance.template_cache.get(templateUri, template_cache_listeners_cf);
			}
			return t;
		}
		catch (Throwable e)
		{
			try
			{
				instance.template_cache.remove(templateUri);
			}
			catch (InterruptedException e1)
			{
				Thread.currentThread().interrupt();
			}

			throw new RuntimeException(e.getCause());
		}
	}

	public static JptInstance getJptInstance(JptConfiguration jptConf, final InputStream instream, final String resourcePath)
	{
		return new JptInstance(jptConf, instream, resourcePath);
	}

	private static boolean isStale(boolean checkModified, JptInstance jpt)
	{
		if (checkModified)
		{
			try
			{
				List<Dependency> dependecies = jpt.getDependecies();

				for (Dependency dependency : dependecies)
				{
					if ((new File(dependency.getUri())).lastModified() > dependency.getLastModified())
					{
						return true;
					}
				}
			}
			catch (Throwable t)
			{
				log.error(t.getMessage(), t);
				return true;
			}
		}
		return false;
	}
}