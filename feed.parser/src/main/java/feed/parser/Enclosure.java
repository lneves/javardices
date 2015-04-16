package feed.parser;

import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Enclosure
{
	private int length;
	private String type;
	private String url;

	public enum Type
	{
		RSS, YAHOO_MEDIA
	}

	public Enclosure()
	{
		super();
	}

	public Enclosure(int length, String type, String url)
	{
		super();
		this.length = length;
		this.type = type;
		this.url = url;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	@JsonIgnore
	@XmlAttribute(name = "length")
	public int getLength()
	{
		return length;
	}

	@XmlAttribute(name = "type")
	public String getType()
	{
		return type;
	}

	@XmlAttribute(name = "url")
	public String getUrl()
	{
		return url;
	}

	@Override
	public String toString()
	{
		return String.format("Enclosure [length=%s, type=%s, url=%s]", length, type, url);
	}
}