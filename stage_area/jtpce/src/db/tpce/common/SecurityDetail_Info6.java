package db.tpce.common;

import java.util.Date;

public class SecurityDetail_Info6
{
	private final Date ni_dts;
	private final String ni_source;
	private final String ni_author;
	private final String ni_item;

	public SecurityDetail_Info6(Date ni_dts, String ni_source, String ni_author, String ni_item)
	{
		super();
		this.ni_dts = ni_dts;
		this.ni_source = ni_source;
		this.ni_author = ni_author;
		this.ni_item = ni_item;
	}

	@Override
	public String toString()
	{
		return String.format("SecurityDetail_Info6 {ni_dts=%s, ni_source=%s, ni_author=%s, ni_item=%s]", ni_dts, ni_source, ni_author, ni_item);
	}
}