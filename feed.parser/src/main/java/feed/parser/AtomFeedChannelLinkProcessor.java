package feed.parser;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;

public class AtomFeedChannelLinkProcessor implements FeedChannelProcessor, FeedEntryProcessor
{
	private final String relValue;
	private final String typeValue;

	public AtomFeedChannelLinkProcessor(String rel, String type)
	{
		super();
		this.relValue = rel;
		this.typeValue = type;
	}

	@Override
	public void process(FeedChannel feed_channel, XMLStreamReader stax_xml_reader)
	{

		String rel = stax_xml_reader.getAttributeValue(null, "rel");
		String type = stax_xml_reader.getAttributeValue(null, "type");
		String href = stax_xml_reader.getAttributeValue(null, "href");

		if (StringUtils.isNotBlank(rel) && StringUtils.isNotBlank(type) && StringUtils.isNotBlank(href) && rel.equals(relValue) && type.equals(typeValue))
		{
			feed_channel.setAttribute("link", href);
		}
	}
}