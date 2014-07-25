package feed.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;

public class CategoryProcessor implements FeedEntryProcessor
{
	public CategoryProcessor()
	{
		super();
	}

	@Override
	public void process(FeedChannel feed_channel, XMLStreamReader stax_xml_reader)
	{
		FeedEntry feed_entry = feed_channel.getLastFeedEntry();

		try
		{
			String atom_category = stax_xml_reader.getAttributeValue("", "term");

			String rss_category = stax_xml_reader.getElementText();

			String category = StringUtils.isBlank(atom_category) ? rss_category : atom_category;

			feed_entry.addCategory(StringUtils.trimToNull(category));
		}
		catch (XMLStreamException e)
		{
			throw new RuntimeException(e);
		}
	}
}