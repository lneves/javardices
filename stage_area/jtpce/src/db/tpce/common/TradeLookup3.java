package db.tpce.common;

import java.math.BigDecimal;
import java.util.Date;

public class TradeLookup3 implements TradeLookup0
{
	public final  long t_id;
	public final  long t_ca_id;
	public final  String t_exec_name;
	public final  boolean t_is_cash;
	public final  BigDecimal t_trade_price;
	public final  int t_qty;
	public final  Date t_dts;
	public final  String t_tt_id;
	public final  BigDecimal se_amt;
	public final  java.sql.Date se_cash_due_date;
	public final  String se_cash_type;
	public final  BigDecimal ct_amt;
	public final String ct_name;

	public TradeLookup3(long t_id, long t_ca_id, String t_exec_name, boolean t_is_cash, BigDecimal t_trade_price, int t_qty, Date t_dts, String t_tt_id, BigDecimal se_amt, java.sql.Date se_cash_due_date, String se_cash_type, BigDecimal ct_amt, String ct_name)
	{
		super();
		this.t_id = t_id;
		this.t_ca_id = t_ca_id;
		this.t_exec_name = t_exec_name;
		this.t_is_cash = t_is_cash;
		this.t_trade_price = t_trade_price;
		this.t_qty = t_qty;
		this.t_dts = t_dts;
		this.t_tt_id = t_tt_id;
		this.se_amt = se_amt;
		this.se_cash_due_date = se_cash_due_date;
		this.se_cash_type = se_cash_type;
		this.ct_amt = ct_amt;
		this.ct_name = ct_name;
	}

	@Override
	public String toString()
	{
		return String.format("TradeLookup3 {t_id=%s, t_ca_id=%s, t_exec_name=%s, t_is_cash=%s, t_trade_price=%s, t_qty=%s, t_dts=%s, t_tt_id=%s, se_amt=%s, se_cash_due_date=%s, se_cash_type=%s, ct_amt=%s, ct_name=%s}", t_id, t_ca_id, t_exec_name, t_is_cash, t_trade_price, t_qty, t_dts, t_tt_id, se_amt, se_cash_due_date, se_cash_type, ct_amt, ct_name);
	}
	
	@Override
	public long getTid()
	{
		return t_id;
	}
}