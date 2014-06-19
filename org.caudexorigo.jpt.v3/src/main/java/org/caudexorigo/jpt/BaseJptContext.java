package org.caudexorigo.jpt;

import java.net.URI;

public class BaseJptContext implements JptContext
{
	private final URI _templateURI;

	public BaseJptContext(URI templateURI)
	{
		_templateURI = templateURI;
	}

	public URI getTemplateURI()
	{
		return _templateURI;
	}
}