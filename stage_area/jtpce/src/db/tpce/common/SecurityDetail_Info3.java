package db.tpce.common;

import java.math.BigDecimal;
import java.sql.Date;

public class SecurityDetail_Info3
{
	public final int fi_year;
	public final int fi_qtr;
	public final java.sql.Date fi_qtr_start_date;
	public final BigDecimal fi_revenue;
	public final BigDecimal fi_net_earn;
	public final BigDecimal fi_basic_eps;
	public final BigDecimal fi_dilut_eps;
	public final BigDecimal fi_margin;
	public final BigDecimal fi_inventory;
	public final BigDecimal fi_assets;
	public final BigDecimal fi_liability;
	public final long fi_out_basic;
	public final long fi_out_dilut;

	public SecurityDetail_Info3(int fi_year, int fi_qtr, Date fi_qtr_start_date, BigDecimal fi_revenue, BigDecimal fi_net_earn, BigDecimal fi_basic_eps, BigDecimal fi_dilut_eps, BigDecimal fi_margin, BigDecimal fi_inventory, BigDecimal fi_assets, BigDecimal fi_liability, long fi_out_basic, long fi_out_dilut)
	{
		super();
		this.fi_year = fi_year;
		this.fi_qtr = fi_qtr;
		this.fi_qtr_start_date = fi_qtr_start_date;
		this.fi_revenue = fi_revenue;
		this.fi_net_earn = fi_net_earn;
		this.fi_basic_eps = fi_basic_eps;
		this.fi_dilut_eps = fi_dilut_eps;
		this.fi_margin = fi_margin;
		this.fi_inventory = fi_inventory;
		this.fi_assets = fi_assets;
		this.fi_liability = fi_liability;
		this.fi_out_basic = fi_out_basic;
		this.fi_out_dilut = fi_out_dilut;
	}

	@Override
	public String toString()
	{
		return String.format("SecurityDetail_Info3 {fi_year=%s, fi_qtr=%s, fi_qtr_start_date=%s, fi_revenue=%s, fi_net_earn=%s, fi_basic_eps=%s, fi_dilut_eps=%s, fi_margin=%s, fi_inventory=%s, fi_assets=%s, fi_liability=%s, fi_out_basic=%s, fi_out_dilut=%s}", fi_year, fi_qtr, fi_qtr_start_date, fi_revenue, fi_net_earn, fi_basic_eps, fi_dilut_eps, fi_margin, fi_inventory, fi_assets, fi_liability, fi_out_basic, fi_out_dilut);
	}
}