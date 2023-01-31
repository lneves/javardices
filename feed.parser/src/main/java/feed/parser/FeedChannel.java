package feed.parser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.text.HtmlStripper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"link", "title", "description", "language", "categories", "entries"})
@XmlType(propOrder = {"link", "title", "description", "language", "atomLink", "categories",
    "entries"})
public class FeedChannel {
  private final Map<String, String> attr;
  private int index = -1;
  private Set<String> col_category;
  private List<FeedEntry> col_entry;
  private final boolean stripHtml;

  public FeedChannel() {
    this(false);
  }

  public FeedChannel(boolean stripHtml) {
    super();
    this.stripHtml = stripHtml;
    attr = new HashMap<String, String>();
    col_category = new HashSet<String>();
    col_entry = new ArrayList<FeedEntry>();
  }

  public void addCategory(String category) {
    if (StringUtils.isNotBlank(category)) {
      col_category.add(StringUtils.trim(category));
    }
  }

  public void addFeedEntry(FeedEntry feedEntry) {
    feedEntry.setStripHhtml(stripHtml);
    col_entry.add(feedEntry);
    index++;
  }

  @JsonIgnore
  @XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
  public AtomLink getAtomLink() {
    return new AtomLink(getLink());
  }

  @XmlElement(name = "category")
  public Set<String> getCategories() {
    Set<String> c = new HashSet<String>();

    c.addAll(col_category);

    return c;
  }

  public void setCategories(Set<String> cat) {
    if (cat != null) {
      col_category = cat;
    }
  }

  @XmlElement(name = "description")
  public String getDescription() {
    String cleanDescription = HtmlStripper.strip(StringEscapeUtils.unescapeHtml4(attr.get(
        "description")));
    return StringUtils.isBlank(cleanDescription) ? null : cleanDescription;
  }

  @XmlElement(name = "item")
  public List<FeedEntry> getEntries() {
    List<FeedEntry> lst = new ArrayList<FeedEntry>();
    lst.addAll(col_entry);
    return lst;
  }

  @XmlElement(name = "language")
  public String getLanguage() {
    return attr.get("language");
  }

  @XmlTransient
  protected FeedEntry getLastFeedEntry() {
    return col_entry.get(index);
  }

  @XmlElement(name = "link")
  public String getLink() {
    return attr.get("link");
  }

  protected boolean getStripHtml() {
    return stripHtml;
  }

  @XmlElement(name = "title")
  public String getTitle() {
    String cleanTitle = HtmlStripper.strip(StringEscapeUtils.unescapeHtml4(attr.get("title")));
    return StringUtils.isBlank(cleanTitle) ? null : cleanTitle;
  }

  public void setAttribute(String key, String value) {
    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
      attr.put(key, value);
    }
  }

  public void setDescription(String description) {
    if (StringUtils.isNotBlank(description)) {
      attr.put("description", description);
    }
  }

  public void setEntries(List<FeedEntry> lst) {
    if (lst != null) {
      col_entry = lst;
    }
  }

  public void setLanguage(String lang) {
    if (StringUtils.isNotBlank(lang)) {
      attr.put("language", lang);
    }
  }

  public void setTitle(String title) {
    if (StringUtils.isNotBlank(title)) {
      attr.put("title", title);
    }
  }

  @Override
  public String toString() {
    return String.format(
        "FeedChannel [link=%s, title=%s, categories=%s, description=%s, \n\tentries=%s]", getLink(),
        getTitle(), getCategories(), getDescription(), getEntries());
  }
}
