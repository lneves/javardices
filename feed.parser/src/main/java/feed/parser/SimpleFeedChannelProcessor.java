package feed.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class SimpleFeedChannelProcessor implements FeedChannelProcessor {
  private final String fieldName;

  public SimpleFeedChannelProcessor(String fieldName) {
    super();
    this.fieldName = fieldName;
  }

  @Override
  public void process(FeedChannel feedChannel, XMLStreamReader staxXmlReader) {
    try {
      String fieldValue = staxXmlReader.getElementText();
      feedChannel.setAttribute(fieldName, fieldValue);
    } catch (XMLStreamException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "SimpleFeedChannelProcessor [fieldName=" + fieldName + "]";
  }
}
