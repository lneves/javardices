package db.tpce.common;

import java.util.Date;

public class TradeHistory
{
	public final long t_id;
	public final String t_s_symb;
	public final int t_qty;
	public final String st_name;
	public final Date th_dts;

	public TradeHistory(long t_id, String t_s_symb, int t_qty, String st_name, Date th_dts)
	{
		super();
		this.t_id = t_id;
		this.t_s_symb = t_s_symb;
		this.t_qty = t_qty;
		this.st_name = st_name;
		this.th_dts = th_dts;
	}

	@Override
	public String toString()
	{
		return String.format("TradeHistory {t_id=%s, t_s_symb=%s, t_qty=%s, st_name=%s, th_dts=%s}", t_id, t_s_symb, t_qty, st_name, th_dts);
	}
}