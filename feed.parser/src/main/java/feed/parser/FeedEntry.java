package feed.parser;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.text.HtmlStripper;
import org.caudexorigo.time.RFC822;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "link", "pubDate", "title", "body", "enclosure", "author", "categories", "guid" })
@XmlType(propOrder = { "link", "rssPubDate", "title", "body", "enclosure", "author", "categories", "guid" })
public class FeedEntry
{
	private static final Pattern img = Pattern.compile(".*<img.*?src=\"(.*?)\".*?>.*");
	private final Map<String, String> attr;
	private Guid guid;
	private Enclosure enclosure;
	private Set<String> col_category;
	private boolean is_clean_body_init = false;
	private String cleanBody = null;
	private boolean stripHtml;

	public FeedEntry()
	{
		this(false);
	}

	public FeedEntry(boolean stripHtml)
	{
		super();
		this.stripHtml = stripHtml;
		attr = new HashMap<String, String>();
		col_category = new HashSet<String>();
	}

	public void addCategory(String category)
	{
		if (StringUtils.isNotBlank(category))
		{
			col_category.add(category.trim());
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeedEntry other = (FeedEntry) obj;
		if (getLink() == null)
		{
			if (other.getLink() != null)
				return false;
		}
		else if (!getLink().equals(other.getLink()))
			return false;
		return true;
	}

	@XmlElement(name = "author")
	public String getAuthor()
	{
		return StringUtils.trimToNull(attr.get("author"));
	}

	@XmlElement(name = "description")
	public String getBody()
	{
		if (stripHtml)
		{
			return getCleanBody();
		}
		else
		{
			return getRawBody();
		}
	}

	@XmlElement(name = "category")
	public Set<String> getCategories()
	{
		Set<String> c = new HashSet<String>();

		c.addAll(col_category);

		return c;
	}

	private String getCleanBody()
	{
		if (!is_clean_body_init)
		{
			cleanBody = StringUtils.trimToNull(HtmlStripper.strip(StringEscapeUtils.unescapeHtml4(getRawBody())));
			is_clean_body_init = true;
		}

		return cleanBody;
	}

	@XmlElement(name = "enclosure")
	public Enclosure getEnclosure()
	{
		if ((enclosure == null) && stripHtml && StringUtils.isNotBlank(getRawBody()))
		{
			Matcher m = img.matcher(getRawBody());

			if (m.find())
			{
				String img_url = m.group(1);
				String img_mtype = ImageMimeTable.getContentType(img_url);

				if (StringUtils.isNotBlank(img_mtype))
				{
					Enclosure e = new Enclosure(1, img_mtype, img_url);
					return e;
				}
			}
		}

		return enclosure;
	}

	@XmlElement(name = "guid")
	public Guid getGuid()
	{
		if (guid == null)
		{
			return new Guid(getLink(), true);
		}
		else
		{
			return guid;
		}
	}

	@XmlElement(name = "link")
	public String getLink()
	{
		String link = p_getLink();

		// if (link != null)
		// {
		// return UrlCodec.encodeUriComponent(link);
		//
		// }
		// else
		// {
		// return link;
		// }
		return link;
	}

	@XmlTransient
	public Date getPubDate()
	{
		if (StringUtils.isNotBlank(attr.get("pubdate")))
		{
			return DateParser.parse(attr.get("pubdate"));
		}
		return null;
	}

	@JsonIgnore
	@XmlTransient
	public String getRawBody()
	{
		if (StringUtils.isNotBlank(attr.get("content:encoded")))
		{
			return attr.get("content:encoded");
		}
		else if (StringUtils.isNotBlank(attr.get("body")))
		{
			return attr.get("body");
		}
		return null;
	}

	@JsonIgnore
	@XmlElement(name = "pubDate")
	public String getRssPubDate()
	{
		if (getPubDate() != null)
		{
			try
			{
				return RFC822.format(getPubDate());
			}
			catch (ParseException e)
			{
				return null;
			}
		}
		return null;
	}

	@XmlElement(name = "title")
	public String getTitle()
	{
		String cleanTitle = HtmlStripper.strip(StringEscapeUtils.unescapeHtml4(attr.get("title")));
		return StringUtils.trimToNull(cleanTitle);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getLink() == null) ? 0 : getLink().hashCode());
		return result;
	}

	private String p_getLink()
	{
		if (StringUtils.isNotBlank(attr.get("origlink")))
		{
			return attr.get("origlink").trim();
		}
		else if (StringUtils.isNotBlank(attr.get("link")))
		{
			return attr.get("link").trim();
		}
		else if ((guid != null) && guid.isPermaLink() && StringUtils.isNotBlank(guid.getValue()))
		{
			return guid.getValue().trim();
		}
		return null;
	}

	public void setAttribute(String key, String value)
	{
		if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value))
		{
			attr.put(key, value);
		}
	}

	protected void setAuthor(String author)
	{
		if (StringUtils.isNotBlank(author))
		{
			attr.put("author", author);
		}
	}

	protected void setBody(String body)
	{
		if (StringUtils.isNotBlank(body))
		{
			attr.put("body", body);
		}
	}

	public void setCategories(Set<String> cat)
	{
		if (cat != null)
		{
			col_category = cat;
		}
	}

	public void setEnclosure(Enclosure enclosure)
	{
		this.enclosure = enclosure;
	}

	public void setGuid(Guid guid)
	{
		if (guid != null)
		{
			this.guid = guid;
		}
	}

	public void setLink(String link)
	{
		if (StringUtils.isNotBlank(link))
		{
			attr.put("link", link);
		}
	}

	public void setPubDate(Date pubdate)
	{
		if (pubdate != null)
		{
			try
			{
				attr.put("pubdate", RFC822.format(pubdate));
			}
			catch (ParseException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public void setTitle(String title)
	{
		if (StringUtils.isNotBlank(title))
		{
			attr.put("title", title);
		}
	}

	protected void setStripHhtml(boolean stripHtml)
	{
		this.stripHtml = stripHtml;
	}

	@Override
	public String toString()
	{
		return String.format("\n\tFeedEntry [author=%s, guid=%s, link=%s, categories%s, pubDate=%s, title=%s, body=%s, enclosure=%s]", getAuthor(), getGuid(), getLink(), getCategories(), getPubDate(), getTitle(), getBody(), getEnclosure());
	}

}