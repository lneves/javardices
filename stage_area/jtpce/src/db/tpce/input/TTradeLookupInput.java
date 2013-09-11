package db.tpce.input;

import java.util.Arrays;
import java.util.Date;

public class TTradeLookupInput
{
	public final int frame_to_execute; // which of the frames to execute
	public final long[] trade_id;
	public final long acct_id;
	public final long max_acct_id;	
	public final int max_trades;
	public final Date start_trade_dts;
	public final Date end_trade_dts;	
	public final String symbol;

	public TTradeLookupInput(int frame_to_execute, long[] trade_id, long acct_id, long max_acct_id, int max_trades, Date start_trade_dts, Date end_trade_dts, String symbol)
	{
		super();
		this.frame_to_execute = frame_to_execute;
		this.trade_id = trade_id;
		this.acct_id = acct_id;
		this.max_acct_id = max_acct_id;		
		this.max_trades = max_trades;		
		this.start_trade_dts = start_trade_dts;
		this.end_trade_dts = end_trade_dts;
		this.symbol = symbol;
	}

	@Override
	public String toString()
	{
		return String.format("TTradeLookupInput [frame_to_execute=%s, trade_id=%s, acct_id=%s, max_acct_id=%s, max_trades=%s, start_trade_dts=%s, end_trade_dts=%s, symbol=%s]", frame_to_execute, Arrays.toString(trade_id), acct_id, max_acct_id, max_trades, start_trade_dts, end_trade_dts, symbol);
	}
}