package feed.parser;

import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class AtomContentProcessor implements FeedEntryProcessor {
  public AtomContentProcessor() {
    super();
  }

  @Override
  public void process(FeedChannel feed_channel, XMLStreamReader stax_xml_reader) {
    FeedEntry feed_entry = feed_channel.getLastFeedEntry();

    try {
      String type = stax_xml_reader.getAttributeValue(null, "type");
      String body = null;

      if (StringUtils.isNotBlank(type) && type.equals("html")) {
        body = stax_xml_reader.getElementText();
      }

      feed_entry.setAttribute("body", body);
    } catch (XMLStreamException e) {
      throw new RuntimeException(e);
    }
  }
}
