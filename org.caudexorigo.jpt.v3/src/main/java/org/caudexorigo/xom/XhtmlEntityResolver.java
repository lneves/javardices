package org.caudexorigo.xom;

import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

public class XhtmlEntityResolver implements EntityResolver2
{
	public XhtmlEntityResolver()
	{
	}

	public InputSource resolveEntity(String publicID, String systemID) throws SAXException
	{
		try
		{
			return this.resolveEntity(null, publicID, null, systemID);
		}
		catch (IOException e)
		{
			throw new SAXException(e);
		}
	}

	@Override
	public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException
	{
		// System.out.println("*************************************************");
		// System.out.println("XhtmlEntityResolver.getExternalSubset.name: " + name);
		// System.out.println("XhtmlEntityResolver.getExternalSubset.baseURI: " + baseURI);

		// if ("html".equals(name))
		//
		// return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/html/html.dtd"));
		// else
		// return new InputSource(new StringReader(""));

		// always return the html dtd
		return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/html/html.dtd"));

	}

	@Override
	public InputSource resolveEntity(String name, String publicID, String baseURI, String systemID) throws SAXException, IOException
	{
		// System.out.println("*************************************************");
		// System.out.println("XhtmlEntityResolver.resolveEntity.name: " + name);
		// System.out.println("XhtmlEntityResolver.resolveEntity.publicID: " + publicID);
		// System.out.println("XhtmlEntityResolver.resolveEntity.baseURI: " + baseURI);
		// System.out.println("XhtmlEntityResolver.resolveEntity.systemID: " + systemID);

		if (systemID.contains(".dtd"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/html/html.dtd"));

		if (systemID.contains("html-lat1.ent"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/html/html-lat1.ent"));
		if (systemID.contains("html-symbol.ent"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/html/html-symbol.ent"));
		if (systemID.contains("html-special.ent"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/html/html-special.ent"));

		else
			return new InputSource(new StringReader(""));
	}
}