package feed.parser;

import javax.xml.stream.XMLStreamReader;

public interface FeedEntryProcessor
{
	public void process(FeedChannel feedChannel, XMLStreamReader staxXmlReader);
}