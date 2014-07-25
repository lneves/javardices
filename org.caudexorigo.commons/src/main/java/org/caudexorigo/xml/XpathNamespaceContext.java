package org.caudexorigo.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class XpathNamespaceContext implements NamespaceContext
{
	private Map<String, String> ns_ctx = new HashMap<String, String>();

	public XpathNamespaceContext()
	{
		super();
		ns_ctx.put("xml", XMLConstants.XML_NS_URI);
	}

	public String getNamespaceURI(String prefix)
	{
		if (prefix == null)
		{
			throw new IllegalArgumentException("Null prefix");
		}
		else
		{
			String uri = ns_ctx.get(prefix);

			if (uri != null)
			{
				return uri;
			}
		}

		return XMLConstants.NULL_NS_URI;
	}

	// This method isn't necessary for XPath processing.
	public String getPrefix(String uri)
	{
		throw new UnsupportedOperationException();
	}

	// This method isn't necessary for XPath processing either.
	public Iterator getPrefixes(String uri)
	{
		throw new UnsupportedOperationException();
	}

	public void addNamespace(String prefix, String uri)
	{
		ns_ctx.put(prefix, uri);
	}

}