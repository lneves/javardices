package org.caudexorigo.jpt;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class JptConfiguration
{

	private static final Logger log = LoggerFactory.getLogger(JptConfiguration.class);

	private static final String CONFIG_FILE = "/jpt.config";

	private static final boolean DEFAULT_CHECK_MODIFIED = true;

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static final boolean DEFAULT_FULL_ERRORS = false;

	private String _active_enviroment;

	private boolean _check_modified;

	private boolean _full_errors;

	private String _encoding;

	private String _xml_reader_class;

	private static JptConfiguration instance = new JptConfiguration();

	private JptConfiguration()
	{
		try
		{
			_active_enviroment = textValueFromPath("/config/active-enviroment/text()");
			_check_modified = Boolean.parseBoolean(textValueFromPath("/config/enviroment/" + _active_enviroment + "/check-modified/text()"));
			_full_errors = Boolean.parseBoolean(textValueFromPath("/config/enviroment/" + _active_enviroment + "/full-errors/text()"));
			_encoding = textValueFromPath("/config/enviroment/" + _active_enviroment + "/encoding/text()");
			_encoding = StringUtils.isBlank(_encoding) ? DEFAULT_ENCODING : _encoding.trim();
			_xml_reader_class = textValueFromPath("/config/enviroment/" + _active_enviroment + "/parser/text()");
		}
		catch (Throwable t)
		{
			log.warn("Error reading configuration check your \"{}\" file. Detail: {}", CONFIG_FILE, t.getMessage());
			_active_enviroment = "";
			_check_modified = DEFAULT_CHECK_MODIFIED;
			_encoding = DEFAULT_ENCODING;
			_full_errors = DEFAULT_FULL_ERRORS;
			_xml_reader_class = "";
		}
	}

	private static String textValueFromPath(String pathExpression)
	{
		try
		{
			Object pad = new Object();
			InputSource configFile = new org.xml.sax.InputSource(pad.getClass().getResourceAsStream(CONFIG_FILE));
			// InputStream is = new FileInputStream(CONFIG_FILE);
			// InputSource configFile = new org.xml.sax.InputSource(is);
			if (configFile.getByteStream() == null)
			{
				throw new RuntimeException("file \"" + CONFIG_FILE + "\" not found in path.");
			}

			// 1. Instantiate an XPathFactory.
			javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
			// 2. Use the XPathFactory to create a new XPath object
			javax.xml.xpath.XPath xpath = factory.newXPath();
			// 3. Compile an XPath string into an XPathExpression
			javax.xml.xpath.XPathExpression expression = xpath.compile(pathExpression);

			// 4. Evaluate the XPath expression on an input document
			String result = expression.evaluate(configFile);
			return result == null ? "" : result;
		}
		catch (Exception e)
		{
			throw new RuntimeException(String.format("Xpath:\"%s\" not found in document!", pathExpression), e);
		}
	}

	public static String active_enviroment()
	{
		return instance._active_enviroment;
	}
	
	public static boolean checkModified()
	{
		return instance._check_modified;
	}

	public static boolean fullErrors()
	{
		return instance._full_errors;
	}

	public static String encoding()
	{
		return instance._encoding;
	}

	public static String xmlReaderClass()
	{
		return instance._xml_reader_class;
	}
}