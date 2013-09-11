package db.tpce.common;

import java.math.BigDecimal;
import java.sql.Date;

public class TradeLookup1 implements TradeLookup0
{
	public final long t_id;
	public final BigDecimal t_bid_price;
	public final String t_exec_name;
	public final boolean t_is_cash;
	public final boolean tt_is_mrkt;
	public final BigDecimal t_trade_price;
	public final BigDecimal se_amt;
	public final java.sql.Date se_cash_due_date;
	public final String se_cash_type;
	public final BigDecimal ct_amt;
	public final String ct_name;

	public TradeLookup1(long t_id, BigDecimal t_bid_price, String t_exec_name, boolean t_is_cash, boolean tt_is_mrkt, BigDecimal t_trade_price, BigDecimal se_amt, Date se_cash_due_date, String se_cash_type, BigDecimal ct_amt, String ct_name)
	{
		super();
		this.t_id = t_id;
		this.t_bid_price = t_bid_price;
		this.t_exec_name = t_exec_name;
		this.t_is_cash = t_is_cash;
		this.tt_is_mrkt = tt_is_mrkt;
		this.t_trade_price = t_trade_price;
		this.se_amt = se_amt;
		this.se_cash_due_date = se_cash_due_date;
		this.se_cash_type = se_cash_type;
		this.ct_amt = ct_amt;
		this.ct_name = ct_name;
	}

	@Override
	public String toString()
	{
		return String.format("TradeLookup1 {t_id=%s, t_bid_price=%s, t_exec_name=%s, t_is_cash=%s, tt_is_mrkt=%s, t_trade_price=%s, se_amt=%s, se_cash_due_date=%s, se_cash_type=%s, ct_amt=%s, ct_name=%s}", t_id, t_bid_price, t_exec_name, t_is_cash, tt_is_mrkt, t_trade_price, se_amt, se_cash_due_date, se_cash_type, ct_amt, ct_name);
	}

	@Override
	public long getTid()
	{
		return t_id;
	}
}