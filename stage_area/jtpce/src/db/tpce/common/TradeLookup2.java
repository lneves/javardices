package db.tpce.common;

import java.math.BigDecimal;
import java.sql.Date;

public class TradeLookup2 implements TradeLookup0
{
	public final long t_id;
	public final String t_exec_name;
	public final BigDecimal t_bid_price;
	public final BigDecimal t_trade_price;
	public final boolean t_is_cash;
	public final BigDecimal se_amt;
	public final java.sql.Date se_cash_due_date;
	public final String se_cash_type;
	public final BigDecimal ct_amt;
	public final String ct_name;

	public TradeLookup2(long t_id, String t_exec_name, BigDecimal t_bid_price, BigDecimal t_trade_price, boolean t_is_cash, BigDecimal se_amt, Date se_cash_due_date, String se_cash_type, BigDecimal ct_amt, String ct_name)
	{
		super();
		this.t_id = t_id;
		this.t_exec_name = t_exec_name;
		this.t_bid_price = t_bid_price;
		this.t_trade_price = t_trade_price;
		this.t_is_cash = t_is_cash;
		this.se_amt = se_amt;
		this.se_cash_due_date = se_cash_due_date;
		this.se_cash_type = se_cash_type;
		this.ct_amt = ct_amt;
		this.ct_name = ct_name;
	}

	@Override
	public String toString()
	{
		return String.format("TradeLookup2 {t_id=%s, t_exec_name=%s, t_bid_price=%s, t_trade_price=%s, t_is_cash=%s, se_amt=%s, se_cash_due_date=%s, se_cash_type=%s, ct_amt=%s, ct_name=%s}", t_id, t_exec_name, t_bid_price, t_trade_price, t_is_cash, se_amt, se_cash_due_date, se_cash_type, ct_amt, ct_name);
	}
	
	@Override
	public long getTid()
	{
		return t_id;
	}
}