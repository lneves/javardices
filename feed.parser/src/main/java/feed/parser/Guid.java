package feed.parser;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Guid
{
	private String value;
	private boolean isPermaLink;

	public Guid()
	{
		super();
	}

	public Guid(String value, boolean permaLink)
	{
		super();
		this.value = value;
		this.isPermaLink = permaLink;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public void setPermaLink(boolean isPermaLink)
	{
		this.isPermaLink = isPermaLink;
	}

	@XmlValue
	public String getValue()
	{
		return value;
	}

	@XmlAttribute(name = "isPermaLink")
	@JsonGetter(value = "isPermaLink")
	public boolean isPermaLink()
	{
		return isPermaLink;
	}

	@Override
	public String toString()
	{
		return String.format("Guid [value=%s, isPermaLink=%s]", value, isPermaLink);
	}
}