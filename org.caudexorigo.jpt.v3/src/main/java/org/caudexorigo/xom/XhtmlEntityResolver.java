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
		if ("html".equals(name))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/xhtml1-20020801/dtd/xhtml1-transitional.dtd"));
		else
			return new InputSource(new StringReader(""));
	}

	@Override
	public InputSource resolveEntity(String name, String publicID, String baseURI, String systemID) throws SAXException, IOException
	{
		if (systemID.contains("xhtml1-strict.dtd"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/xhtml1-20020801/dtd/xhtml1-strict.dtd"));
		if (systemID.contains("xhtml1-transitional.dtd"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/xhtml1-20020801/dtd/xhtml1-transitional.dtd"));
		if (systemID.contains("xhtml1-frameset.dtd"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/xhtml1-20020801/dtd/xhtml1-frameset.dtd"));
		if (systemID.contains("xhtml-lat1.ent"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/xhtml1-20020801/dtd/xhtml-lat1.ent"));
		if (systemID.contains("xhtml-symbol.ent"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/xhtml1-20020801/dtd/xhtml-symbol.ent"));
		if (systemID.contains("xhtml-special.ent"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/xhtml1-20020801/dtd/xhtml-special.ent"));
		if (systemID.contains("xhtml11.dtd"))
			return new InputSource(getClass().getResourceAsStream("/org/caudexorigo/etc/catalogs/xhtml11-20010531/dtd/xhtml11-flat.dtd"));
		else
			return new InputSource(new StringReader(""));
	}
}
