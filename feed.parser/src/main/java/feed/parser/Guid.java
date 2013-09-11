package feed.parser;

public class Guid
{
	private final String value;
	private final boolean permaLink;

	public Guid(String value, boolean permaLink)
	{
		super();
		this.value = value;
		this.permaLink = permaLink;
	}

	public String getValue()
	{
		return value;
	}

	public boolean isPermaLink()
	{
		return permaLink;
	}

	@Override
	public String toString()
	{
		return "Guid [permaLink=" + permaLink + ", value=" + value + "]";
	}
}