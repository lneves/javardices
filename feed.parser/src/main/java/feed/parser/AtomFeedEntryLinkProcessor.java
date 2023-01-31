package feed.parser;

import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamReader;

public class AtomFeedEntryLinkProcessor implements FeedEntryProcessor {
  private final String relValue;
  private final String typeValue;

  public AtomFeedEntryLinkProcessor(String rel, String type) {
    super();
    this.relValue = rel;
    this.typeValue = type;
  }

  @Override
  public void process(FeedChannel feed_channel, XMLStreamReader stax_xml_reader) {

    FeedEntry feed_entry = feed_channel.getLastFeedEntry();
    String rel = stax_xml_reader.getAttributeValue("", "rel");
    String type = stax_xml_reader.getAttributeValue("", "type");
    String href = stax_xml_reader.getAttributeValue("", "href");

    if (StringUtils.isNotBlank(rel) && StringUtils.isNotBlank(type) && StringUtils.isNotBlank(href)
        && rel.equals(relValue) && type.equals(typeValue)) {
      feed_entry.setAttribute("link", href);
    }
  }
}
