package feed.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.caudexorigo.text.HtmlStripper;
import org.caudexorigo.text.StringEscapeUtils;
import org.caudexorigo.text.StringUtils;

public class FeedEntry
{
	private final Map<String, String> attr;
	private Guid guid;
	private Enclosure enclosure;
	private Set<String> lst_category;
	private boolean is_clean_body_init = false;
	private String cleanBody = null;

	public FeedEntry()
	{
		super();
		attr = new HashMap<String, String>();
		lst_category = new HashSet<String>();
	}

	public void addCategory(String category)
	{
		if (StringUtils.isNotBlank(category))
		{
			lst_category.add(category.trim());
		}
	}

	public String getAuthor()
	{
		return StringUtils.trimToNull(attr.get("author"));
	}

	public String getBody()
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

	public String[] getCategories()
	{
		if (lst_category.size() > 0)
		{
			return lst_category.toArray(new String[lst_category.size()]);
		}
		else
		{
			return new String[0];
		}
	}

	public String getCleanBody()
	{
		if (!is_clean_body_init)
		{
			cleanBody = StringUtils.trimToNull(HtmlStripper.strip(StringEscapeUtils.unescapeHtml(getBody())));
			is_clean_body_init = true;
		}

		return cleanBody;
	}

	public Enclosure getEnclosure()
	{
		return enclosure;
	}

	public Guid getGuid()
	{
		return guid;
	}

	public String getLink()
	{
		String link = p_getLink();

		if (link != null)
		{
			return encodeLink(link);

		}
		else
		{
			return link;
		}
	}

	private String encodeLink(String link)
	{
		StringBuilder sb = new StringBuilder();
		char[] chars = link.toCharArray();

		for (char ch : chars)
		{
			if (ch == ' ')
			{
				sb.append("%20");
			}
			else if (ch > 127)
			{
				sb.append(encodeChar(ch));
			}
			else
			{
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	private String encodeChar(char ch)
	{
		try
		{
			char[] uc = new char[1];
			uc[0] = ch;
			String u = new String(uc);
			return URLEncoder.encode(u, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
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

	public Date getPubDate()
	{
		if (StringUtils.isNotBlank(attr.get("pubdate")))
		{
			return DateParser.parse(attr.get("pubdate"));
		}
		return null;
	}

	public String getTitle()
	{
		String cleanTitle = HtmlStripper.strip(StringEscapeUtils.unescapeHtml(attr.get("title")));
		return StringUtils.trimToNull(cleanTitle);
	}

	public void setAttribute(String key, String value)
	{
		if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value))
		{
			attr.put(key, value);
		}
	}

	protected void setGuid(Guid guid)
	{
		if (guid != null)
		{
			this.guid = guid;
		}
	}

	public void setEnclosure(Enclosure enclosure)
	{
		this.enclosure = enclosure;
	}

	@Override
	public String toString()
	{
		return String.format("\n\tFeedEntry [author=%s, guid=%s, link=%s, categories%s, pubDate=%s, title=%s, body=%s, enclosure=%s]", getAuthor(), getGuid(), getLink(), Arrays.toString(getCategories()), getPubDate(), getTitle(), getCleanBody(), getEnclosure());
	}
}