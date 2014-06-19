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
			_lastModified = (new File(_uri)).lastModified();
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