package org.caudexorigo.jpt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.nu.xom.Builder;
import org.caudexorigo.nu.xom.Document;
import org.caudexorigo.nu.xom.ParsingException;
import org.caudexorigo.nu.xom.ValidityException;
import org.caudexorigo.nu.xom.XMLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public final class XomDocumentBuilder
{
	private static final Logger log = LoggerFactory.getLogger(XomDocumentBuilder.class);

	private static final XhtmlEntityResolver xhtmlEntityResolver = new XhtmlEntityResolver();

	private static final XMLReader reader = getParser();

	private static XMLReader getParser()
	{
		try
		{
			XMLReader reader0 = XMLReaderFactory.createXMLReader();
			setParserFeature(reader0, "http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
			setParserFeature(reader0, "http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			setParserFeature(reader0, "http://xml.org/sax/features/external-general-entities", false);
			setParserFeature(reader0, "http://xml.org/sax/features/external-parameter-entities", false);

			reader0.setEntityResolver(xhtmlEntityResolver);
			return reader0;
		}
		catch (SAXException ex)
		{
			throw new XMLException("Could not find a suitable SAX2 parser", ex);
		}
	}

	private static void setParserFeature(XMLReader xmlReader, String feature, boolean value)
	{
		try
		{
			xmlReader.setFeature(feature, value);
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	public static Document getDocument(InputStream in) throws ValidityException, ParsingException, IOException
	{
		synchronized (reader)
		{
			if (reader == null)
			{
				XMLReader singularity_reader = getParser();
				Builder builder = new Builder(singularity_reader, false);
				return builder.build(in);
			}

			Builder builder = new Builder(reader, false);
			return builder.build(in);
		}
	}

	public static Document getDocument(URI templateUri) throws ValidityException, ParsingException, IOException
	{
		if (log.isDebugEnabled())
		{
			log.debug("building document: {}", templateUri.toString());
		}

		try (InputStream in = uri2InputStream(templateUri))
		{
			return getDocument(in);
		}
		catch (FileNotFoundException e)
		{
			throw new JptNotFoundException(e);
		}
	}

	private static InputStream uri2InputStream(URI templateUri) throws IOException, MalformedURLException
	{
		InputStream in;
		if ("jar".equals(templateUri.getScheme()))
		{
			String path = StringUtils.substringAfterLast(templateUri.toString(), "!");

			in = XomDocumentBuilder.class.getResourceAsStream(path);
		}
		else
		{
			in = templateUri.toURL().openStream();
		}
		return in;
	}

	private XomDocumentBuilder()
	{
	}
}