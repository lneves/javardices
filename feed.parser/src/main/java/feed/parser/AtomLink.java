package feed.parser;

import jakarta.xml.bind.annotation.XmlAttribute;

public class AtomLink {
  private final String href;

  public AtomLink(String href) {
    super();
    this.href = href;
  }

  @XmlAttribute(name = "href")
  public String getHref() {
    return href;
  }

  @XmlAttribute(name = "type")
  public String getType() {
    return "application/rss+xml";
  }

  @XmlAttribute(name = "rel")
  public String getRel() {
    return "self";
  }
}
