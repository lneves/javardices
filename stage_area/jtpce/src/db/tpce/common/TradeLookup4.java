package db.tpce.common;

public class TradeLookup4
{
	public final  long hh_h_t_id;
	public final  long hh_t_id;
	public final  int hh_before_qty;
	public final  int hh_after_qty;

	public TradeLookup4(long hh_h_t_id, long hh_t_id, int hh_before_qty, int hh_after_qty)
	{
		super();
		this.hh_h_t_id = hh_h_t_id;
		this.hh_t_id = hh_t_id;
		this.hh_before_qty = hh_before_qty;
		this.hh_after_qty = hh_after_qty;
	}

	@Override
	public String toString()
	{
		return String.format("TradeLookup4 {hh_h_t_id=%s, hh_t_id=%s, hh_before_qty=%s, hh_after_qty=%s}", hh_h_t_id, hh_t_id, hh_before_qty, hh_after_qty);
	}
}