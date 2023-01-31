package feed.parser;

import javax.xml.stream.XMLStreamReader;

public interface FeedChannelProcessor {
  public void process(FeedChannel feedChannel, XMLStreamReader staxXmlReader);
}
