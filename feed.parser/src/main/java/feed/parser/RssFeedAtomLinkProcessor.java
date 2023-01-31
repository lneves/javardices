package feed.parser;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;

public class RssFeedAtomLinkProcessor implements FeedEntryProcessor
{
	public RssFeedAtomLinkProcessor()
	{
		super();
	}

	@Override
	public void process(FeedChannel feed_channel, XMLStreamReader stax_xml_reader)
	{

		String href = stax_xml_reader.getAttributeValue(null, "href");

		if (StringUtils.isNotBlank(href))
		{
			FeedEntry feed_entry = feed_channel.getLastFeedEntry();

			feed_entry.setAttribute("link", href);
		}
	}
}