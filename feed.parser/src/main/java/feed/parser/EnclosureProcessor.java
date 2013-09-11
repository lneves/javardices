package feed.parser;

import javax.xml.stream.XMLStreamReader;

import org.caudexorigo.text.StringUtils;

import feed.parser.Enclosure.Type;

public class EnclosureProcessor implements FeedEntryProcessor
{
	private final Type encType;

	public EnclosureProcessor(Type encType)
	{
		this.encType = encType;
	}

	@Override
	public void process(FeedChannel feed_channel, XMLStreamReader stax_xml_reader)
	{
		FeedEntry feed_entry = feed_channel.getLastFeedEntry();

		try
		{
			switch (encType)
			{
			case RSS:
				rssMedia(stax_xml_reader, feed_entry);
				break;
			case YAHOO_MEDIA:
				yahooMedia(stax_xml_reader, feed_entry);
				break;

			default:
				break;
			}

		}
		catch (Throwable e)
		{
			throw new RuntimeException(e);
		}
	}

	private void rssMedia(XMLStreamReader stax_xml_reader, FeedEntry feed_entry)
	{
		int lenght = parse(stax_xml_reader.getAttributeValue("", "lenght"));
		String type = stax_xml_reader.getAttributeValue("", "type");
		String url = stax_xml_reader.getAttributeValue("", "url");

		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(url))
		{
			Enclosure enclosure = new Enclosure(lenght, type, url);

			feed_entry.setEnclosure(enclosure);
		}
	}

	private void yahooMedia(XMLStreamReader stax_xml_reader, FeedEntry feed_entry)
	{
		int lenght = 1;
		String type = stax_xml_reader.getAttributeValue("", "type");
		String url = stax_xml_reader.getAttributeValue("", "url");

		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(url))
		{
			Enclosure enclosure = new Enclosure(lenght, type, url);

			feed_entry.setEnclosure(enclosure);
		}
	}

	private int parse(String s)
	{
		try
		{
			return Integer.parseInt(s);
		}
		catch (Throwable e)
		{
			return 1;
		}
	}
}