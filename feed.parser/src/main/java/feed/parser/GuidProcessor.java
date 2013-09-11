package feed.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class GuidProcessor implements FeedEntryProcessor
{
	public GuidProcessor()
	{
		super();
	}

	@Override
	public void process(FeedChannel feed_channel, XMLStreamReader stax_xml_reader)
	{
		FeedEntry feed_entry = feed_channel.getLastFeedEntry();

		try
		{
			boolean isPermaLink = Boolean.parseBoolean(stax_xml_reader.getAttributeValue(null, "isPermaLink"));
			String value = stax_xml_reader.getElementText();

			Guid guid = new Guid(value, isPermaLink);

			feed_entry.setGuid(guid);
		}
		catch (XMLStreamException e)
		{
			throw new RuntimeException(e);
		}
	}
}