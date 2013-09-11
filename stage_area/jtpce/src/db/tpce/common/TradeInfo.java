package db.tpce.common;

import java.math.BigDecimal;
import java.util.Date;

public class TradeInfo
{
	private final String cust_l_name;
	private final String cust_f_name;
	private final String broker_name;
	private final BigDecimal charge;
	private final String exec_name;
	private final String ex_name;
	private final String s_name;
	private final String status_name;
	private final String symbol;
	private final Date trade_dts;
	private final long trade_id;
	private final int trade_qty;
	private final String type_name;

	public TradeInfo(String cust_l_name, String cust_f_name, String broker_name, BigDecimal charge, String exec_name, String ex_name, String s_name, String status_name, String symbol, Date trade_dts, long trade_id, int trade_qty, String type_name)
	{
		super();
		this.cust_l_name = cust_l_name;
		this.cust_f_name = cust_f_name;
		this.broker_name = broker_name;
		this.charge = charge;
		this.exec_name = exec_name;
		this.ex_name = ex_name;
		this.s_name = s_name;
		this.status_name = status_name;
		this.symbol = symbol;
		this.trade_dts = trade_dts;
		this.trade_id = trade_id;
		this.trade_qty = trade_qty;
		this.type_name = type_name;
	}

	@Override
	public String toString()
	{
		return String.format("TradeInfo {cust_l_name=%s, cust_f_name=%s, broker_name=%s, charge=%s, exec_name=%s, ex_name=%s, s_name=%s, status_name=%s, symbol=%s, trade_dts=%s, trade_id=%s, trade_qty=%s, type_name=%s}", cust_l_name, cust_f_name, broker_name, charge, exec_name, ex_name, s_name, status_name, symbol, trade_dts, trade_id, trade_qty, type_name);
	}
}