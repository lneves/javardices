package feed.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.text.HtmlStripper;

public class FeedChannel
{
	private final Map<String, String> attr;
	private int index = -1;
	private List<String> lst_category;
	private final List<FeedEntry> lst_entry;

	public FeedChannel()
	{
		super();
		attr = new HashMap<String, String>();
		lst_entry = new ArrayList<FeedEntry>();
		lst_category = new ArrayList<String>();
	}

	public void addCategory(String category)
	{
		if (StringUtils.isNotBlank(category))
		{
			lst_category.add(category.trim());
		}
	}

	public void addFeedEntry(FeedEntry feedEntry)
	{
		lst_entry.add(feedEntry);
		index++;
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

	public String getLanguage()
	{
		return attr.get("language");
	}

	public String getDescription()
	{
		String cleanDescription = HtmlStripper.strip(StringEscapeUtils.unescapeHtml4(attr.get("description")));
		return StringUtils.isBlank(cleanDescription) ? null : cleanDescription;
	}

	public FeedEntry[] getEntries()
	{
		if (lst_entry.size() > 0)
		{
			return lst_entry.toArray(new FeedEntry[lst_entry.size()]);
		}
		else
		{
			return new FeedEntry[0];
		}
	}

	public FeedEntry getLastFeedEntry()
	{
		return lst_entry.get(index);
	}

	public String getLink()
	{
		return attr.get("link");
	}

	public String getTitle()
	{
		String cleanTitle = HtmlStripper.strip(StringEscapeUtils.unescapeHtml4(attr.get("title")));
		return StringUtils.isBlank(cleanTitle) ? null : cleanTitle;
	}

	public void setAttribute(String key, String value)
	{
		if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value))
		{
			attr.put(key, value);
		}
	}

	@Override
	public String toString()
	{
		return String.format("FeedChannel [link=%s, title=%s, categories=%s, description=%s, \n\tentries=%s]", getLink(), getTitle(), Arrays.toString(getCategories()), getDescription(), Arrays.toString(getEntries()));
	}
}