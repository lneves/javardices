package db.tpce.input;


public class TMarketWatchFrame1Input
{
	public final long c_id;
	public final long acct_id;
	public final long starting_co_id;
	public final long ending_co_id;
	public final String industry_name;
	public final java.sql.Date start_date;

	public TMarketWatchFrame1Input(long c_id, long acct_id, long starting_co_id, long ending_co_id, String industry_name, java.sql.Date start_date)
	{
		super();
		this.acct_id = acct_id;
		this.c_id = c_id;
		this.ending_co_id = ending_co_id;
		this.starting_co_id = starting_co_id;
		this.industry_name = industry_name;
		this.start_date = start_date;
	}

	@Override
	public String toString()
	{
		return String.format("TMarketWatchFrame1Input [c_id=%s, acct_id=%s, starting_co_id=%s, ending_co_id=%s, industry_name=%s, start_date=%s]", c_id, acct_id, starting_co_id, ending_co_id, industry_name, start_date);
	}
}