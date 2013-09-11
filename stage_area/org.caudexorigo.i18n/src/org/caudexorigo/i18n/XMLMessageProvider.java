package org.caudexorigo.i18n;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.caudexorigo.ErrorAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The <code>XMLMessageProvider</code> provides messages defined in an XML format.
 * 
 */
public class XMLMessageProvider implements MessageProvider
{
	private static final Logger logger = LoggerFactory.getLogger(XMLMessageProvider.class.getName());

	private static SAXParserFactory factory = SAXParserFactory.newInstance();

	private Map<String, Message> messages = new HashMap<String, Message>();

	public XMLMessageProvider(InputStream inputStream)
	{
		try
		{
			Map<String, Message> applicationMessages = new HashMap<String, Message>();
			SAXParser parser = factory.newSAXParser();
			ConfigurationHandler handler = new ConfigurationHandler();
			parser.parse(new InputSource(inputStream), handler);
			Map<String, Message> parsedMessages = handler.getMessages();
			applicationMessages.putAll(parsedMessages);
			messages.putAll(applicationMessages);
		}
		catch (Throwable t)
		{
			// TODO: Fix message without ID
			// logger.error(I18nUtils.INTERNAL_MESSAGES.getString(I18nUtils.MESSAGE_PARSING_ERROR),
			// exception);
			// TODO: Consider throwing exception
			Throwable r = ErrorAnalyser.findRootCause(t);
			logger.warn(String.format("Translations will not be available. %s", r.getMessage()), r);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see internal.org.apache.commons.i18n.MessageProvider#getText(java.lang.String, java.lang.String, java.util.Locale)
	 */
	public String getText(String id, String entry, Locale locale) throws MessageNotFoundException
	{
		Message message = findMessage(id, locale);
		return message.getEntry(entry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see internal.org.apache.commons.i18n.MessageProvider#getEntries(java.lang.String, java.util.Locale)
	 */
	public Map<String, String> getEntries(String id, Locale locale) throws MessageNotFoundException
	{
		Message message = findMessage(id, locale);
		return message.getEntries();
	}

	private Message findMessage(String id, Locale locale)
	{
		Message message = lookupMessage(id, locale);
		if (message == null)
		{
			message = lookupMessage(id, Locale.getDefault());
		}
		if (message == null)
			throw new MessageNotFoundException(MessageFormat.format(I18nUtils.INTERNAL_MESSAGES.getString(I18nUtils.MESSAGE_NOT_FOUND), new Object[] { id }));
		return message;
	}

	private Message lookupMessage(String id, Locale locale)
	{
		String key = id + '_' + locale.toString();
		if (messages.containsKey(key))
			return messages.get(key);
		locale = I18nUtils.getParentLocale(locale);
		while (locale != null)
		{
			key = id + '_' + locale.toString();
			if (messages.containsKey(key))
				return messages.get(key);
			locale = I18nUtils.getParentLocale(locale);
		}
		return null;
	}

	class ConfigurationHandler extends DefaultHandler
	{
		private String id, key;

		private Message message;

		private StringBuilder cData;

		public void startElement(String namespaceUri, String localeName, String qName, Attributes attributes)
		{
			if (qName.matches("message"))
			{
				id = attributes.getValue("id");
			}
			else if (qName.matches("locale"))
			{
				message = new Message(id);
				message.setLanguage(attributes.getValue("language"));
				message.setCountry(attributes.getValue("country"));
				message.setVariant(attributes.getValue("variant"));
			}
			else if (qName.matches("entry"))
			{
				key = attributes.getValue("key");
				cData = new StringBuilder();
			}
		}

		public void characters(char[] ch, int start, int length)
		{
			if (message != null && key != null && length > 0)
			{
				cData.append(ch, start, length);
			}
		}

		public void endElement(String namespaceUri, String localeName, String qName)
		{
			if (qName.matches("locale"))
			{
				messages.put(message.getKey(), message);
			}
			else if (qName.matches("entry"))
			{
				message.addEntry(key, cData.toString());
				key = null;
			}
		}

		Map<String, Message> getMessages()
		{
			return messages;
		}
	}

	static class Message
	{
		private final String id;

		private String language, country, variant;

		private Map<String, String> entries = new HashMap<String, String>();

		public Message(String id)
		{
			this.id = id;
		}

		public void addEntry(String key, String value)
		{
			entries.put(key, value);
		}

		public String getEntry(String key)
		{
			return entries.get(key);
		}

		public Map<String, String> getEntries()
		{
			return entries;
		}

		public void setLanguage(String language)
		{
			this.language = language;
		}

		public void setCountry(String country)
		{
			this.country = country;
		}

		public void setVariant(String variant)
		{
			this.variant = variant;
		}

		public String getKey()
		{
			return id + '_' + new Locale((language != null) ? language : "", (country != null) ? country : "", (variant != null) ? variant : "").toString();
		}
	}
}