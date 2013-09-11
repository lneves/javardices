package db.tpce.input;

import java.sql.Date;

public class TSecurityDetailInput
{
	public final boolean access_lob_flag;
	public final int max_rows_to_return;
	public final java.sql.Date start_day;
	public final String symbol;

	public TSecurityDetailInput(boolean access_lob_flag, int max_rows_to_return, Date start_day, String symbol)
	{
		super();
		this.access_lob_flag = access_lob_flag;
		this.max_rows_to_return = max_rows_to_return;
		this.start_day = start_day;
		this.symbol = symbol;
	}

	@Override
	public String toString()
	{
		return String.format("TSecurityDetailInput [access_lob_flag=%s, max_rows_to_return=%s, start_day=%s, symbol=%s]", access_lob_flag, max_rows_to_return, start_day, symbol);
	}
}