package org.caudexorigo.jpt;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.caudexorigo.ds.Cache;
import org.caudexorigo.ds.CacheFiller;
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

	private static final CacheFiller<URI, JptInstance> template_cache_listeners_cf = new CacheFiller<URI, JptInstance>()
	{
		public JptInstance populate(URI templateUri)
		{
			return new JptInstance(templateUri);
		}
	};

	public static JptInstance getJptInstance(final URI templateUri)
	{
		try
		{
			JptInstance t = instance.template_cache.get(templateUri, template_cache_listeners_cf);
			boolean isStale = isStale(t);
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

	public static JptInstance getJptInstance(final InputStream in_io, final String resourcePath)
	{
		return new JptInstance(in_io, resourcePath);
	}

	private static boolean isStale(JptInstance jpt)
	{
		if (JptConfiguration.checkModified())
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