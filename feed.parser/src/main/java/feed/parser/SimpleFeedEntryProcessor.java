package feed.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class SimpleFeedEntryProcessor implements FeedEntryProcessor {
  private final String fieldName;

  public SimpleFeedEntryProcessor(String fieldName) {
    super();
    this.fieldName = fieldName;
  }

  @Override
  public void process(FeedChannel feed_channel, XMLStreamReader stax_xml_reader) {
    FeedEntry feed_entry = feed_channel.getLastFeedEntry();

    try {
      String fieldValue = stax_xml_reader.getElementText();
      feed_entry.setAttribute(fieldName, fieldValue);
    } catch (XMLStreamException e) {
      throw new RuntimeException(e);
    }
  }
}
