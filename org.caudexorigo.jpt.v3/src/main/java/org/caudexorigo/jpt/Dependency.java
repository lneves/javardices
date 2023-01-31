package org.caudexorigo.jpt;

import java.io.File;
import java.net.URI;

public class Dependency
{
	public Dependency(URI templateUri)
	{
		if (templateUri == null)
		{
			throw new IllegalArgumentException("A blank path can not be used to create a Dependency.");
		}
		else
		{
			_uri = templateUri;

			long mtime = 0L;

			try
			{
				mtime = (new File(_uri)).lastModified();
			}
			catch (Throwable t)
			{
				// could not create a file from the specified URI
			}

			_lastModified = mtime;
		}
	}

	public long getLastModified()
	{
		return _lastModified;
	}

	public URI getUri()
	{
		return _uri;
	}

	private final URI _uri;
	private final long _lastModified;
}