package feed.parser;

import javax.xml.stream.XMLStreamReader;

public class AddFeedEntryProcessor implements FeedChannelProcessor
{
	@Override
	public void process(FeedChannel feedChannel, XMLStreamReader staxXmlReader)
	{
		feedChannel.addFeedEntry(new FeedEntry(feedChannel.getStripHtml()));
	}
}