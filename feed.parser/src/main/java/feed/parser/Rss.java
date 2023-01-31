package feed.parser;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
public class Rss {
  private FeedChannel channel;

  public Rss() {
    super();
  }

  public Rss(FeedChannel channel) {
    super();
    this.channel = channel;
  }

  @XmlElement(name = "channel")
  public FeedChannel getChannel() {
    return channel;
  }

  @JsonIgnore
  @XmlAttribute(name = "version")
  public String getVersion() {
    return "2.0";
  }

  public void setChannel(FeedChannel channel) {
    this.channel = channel;
  }
}
