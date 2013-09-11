package feed.parser;

public class Enclosure
{
	private final int lenght;
	private final String type;
	private final String url;

	public enum Type
	{
		RSS, YAHOO_MEDIA
	}

	public Enclosure(int lenght, String type, String url)
	{
		super();
		this.lenght = lenght;
		this.type = type;
		this.url = url;
	}

	public int getLenght()
	{
		return lenght;
	}

	public String getType()
	{
		return type;
	}

	public String getUrl()
	{
		return url;
	}

	@Override
	public String toString()
	{
		return String.format("Enclosure [lenght=%s, type=%s, url=%s]", lenght, type, url);
	}
}