package db.tpce.common;

import java.util.Date;

public class TradeLookupHistory
{
	private final Date th_dts;
	private final String th_st_id;

	public TradeLookupHistory(Date th_dts, String th_st_id)
	{
		super();
		this.th_dts = th_dts;
		this.th_st_id = th_st_id;
	}

	@Override
	public String toString()
	{
		return "TradeLookupHistory {th_dts=" + th_dts + ", th_st_id=" + th_st_id + "}";
	}
}