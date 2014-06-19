package org.caudexorigo.xom;

import java.io.InputStream;

import nu.xom.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafeDocumentBuilder
{
	private static Logger log = LoggerFactory.getLogger(SafeDocumentBuilder.class);

	public static Document getDocument(String path)
	{
		log.debug("document.path: '{}'", path);
		try
		{
			InputStream in = SafeDocumentBuilder.class.getResourceAsStream(path);
			return XomDocumentBuilder.getDocument(in);
		}
		catch (Throwable t)
		{
			log.error(t.getMessage());
			return null;
		}
	}
}