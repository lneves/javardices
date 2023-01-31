package org.caudexorigo.jpt;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class JptConfiguration
{
	private static final boolean DEFAULT_CHECK_MODIFIED = true;

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static final boolean DEFAULT_FULL_ERRORS = false;

	private static final boolean DEFAULT_ESCAPE_OUTPUT = true;

	public static final JptConfiguration DEFAULT_CONFIG = new JptConfiguration(DEFAULT_CHECK_MODIFIED, DEFAULT_FULL_ERRORS, DEFAULT_ESCAPE_OUTPUT, DEFAULT_ENCODING);

	public static final JptConfiguration DISABLE_OUTPUT_ESCAPING = new JptConfiguration(DEFAULT_CHECK_MODIFIED, DEFAULT_FULL_ERRORS, false, DEFAULT_ENCODING);

	public static JptConfiguration fromFile(final String configPath)
	{
		try
		{
			InputSource sourceFile = new org.xml.sax.InputSource(JptConfiguration.class.getResourceAsStream(configPath));
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(sourceFile);
			XPath xPath = XPathFactory.newInstance().newXPath();

			String activeEnviroment = textValueFromPath(xmlDocument, xPath, "/config/active-enviroment/text()");
			boolean checkModified = tryParse(xmlDocument, xPath, "/config/enviroment/" + activeEnviroment + "/check-modified/text()", true);
			boolean fullErrors = tryParse(xmlDocument, xPath, "/config/enviroment/" + activeEnviroment + "/full-errors/text()", true);
			boolean escapeOutput = tryParse(xmlDocument, xPath, "/config/enviroment/" + activeEnviroment + "/escape-output/text()", true);
			String encoding = textValueFromPath(xmlDocument, xPath, "/config/enviroment/" + activeEnviroment + "/encoding/text()");
			encoding = StringUtils.isBlank(encoding) ? DEFAULT_ENCODING : encoding.trim();

			JptConfiguration conf = new JptConfiguration(checkModified, fullErrors, escapeOutput, encoding);
			conf.activeEnviroment = activeEnviroment;
			return conf;
		}
		catch (Throwable t)
		{
			String emsg = String.format("Error reading configuration from \"%s\". Detail: %s", configPath, ExceptionUtils.getRootCauseMessage(t));
			throw new IllegalArgumentException(emsg);
		}
	}

	private static boolean tryParse(Document xmlDocument, XPath xPath, String expression, boolean defaultValue)
	{
		try
		{
			String svalue = textValueFromPath(xmlDocument, xPath, expression);

			if (StringUtils.isBlank(svalue))
			{
				return defaultValue;
			}
			else
			{
				return Boolean.parseBoolean(svalue);
			}

		}
		catch (Throwable t)
		{
			return defaultValue;
		}
	}

	private static String textValueFromPath(Document xmlDocument, XPath xPath, String pathExpression)
	{
		try
		{
			String result = xPath.compile(pathExpression).evaluate(xmlDocument);
			return result == null ? "" : result;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public JptConfiguration(boolean checkModified, boolean fullErrors, boolean escapeOutput, String encoding)
	{
		super();
		_checkModified = checkModified;
		_fullErrors = fullErrors;
		_escapeOutput = escapeOutput;
		_encoding = encoding;
	}

	private final boolean _checkModified;

	private final boolean _fullErrors;

	private final boolean _escapeOutput;

	private final String _encoding;

	private String activeEnviroment;

	public boolean checkModified()
	{
		return _checkModified;
	}

	public boolean fullErrors()
	{
		return _fullErrors;
	}

	public boolean escapeOutput()
	{
		return _escapeOutput;
	}

	public String encoding()
	{
		return _encoding;
	}

	public String getActiveEnviroment()
	{
		return StringUtils.defaultIfBlank(activeEnviroment, "");
	}

	@Override
	public String toString()
	{
		return String.format("JptConfiguration [activeEnviroment=%s, checkModified=%s, fullErrors=%s, escapeOutput=%s, encoding=%s]", activeEnviroment, _checkModified, _fullErrors, _escapeOutput, _encoding);
	}

}