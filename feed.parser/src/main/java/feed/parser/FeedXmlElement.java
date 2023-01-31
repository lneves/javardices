package feed.parser;

public class FeedXmlElement {
  private final String prefix;
  private final String localName;

  public FeedXmlElement(String prefix, String localName) {
    super();
    this.prefix = prefix;
    this.localName = localName;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getLocalName() {
    return localName;
  }

  @Override
  public String toString() {
    return prefix + ":" + localName;
  }
}
