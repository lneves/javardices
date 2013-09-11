package db.tpce.common;

import java.util.Date;

public class SecurityDetail_Info7
{
	private final Date ni_dts;
	private final String ni_source;
	private final String ni_author;
	private final String ni_headline;
	private final String ni_summary;

	public SecurityDetail_Info7(Date ni_dts, String ni_source, String ni_author, String ni_headline, String ni_summary)
	{
		super();
		this.ni_dts = ni_dts;
		this.ni_source = ni_source;
		this.ni_author = ni_author;
		this.ni_headline = ni_headline;
		this.ni_summary = ni_summary;
	}

	@Override
	public String toString()
	{
		return String.format("SecurityDetail_Info7 {ni_dts=%s, ni_source=%s, ni_author=%s, ni_headline=%s, ni_summary=%s}", ni_dts, ni_source, ni_author, ni_headline, ni_summary);
	}
}