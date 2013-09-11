package db.tpce.common;

import java.math.BigDecimal;

public class SecurityDetail_Info5
{
	private final BigDecimal lt_price;
	private final BigDecimal lt_open_price;
	private final long lt_vol;

	public SecurityDetail_Info5(BigDecimal lt_price, BigDecimal lt_open_price, long lt_vol)
	{
		super();
		this.lt_price = lt_price;
		this.lt_open_price = lt_open_price;
		this.lt_vol = lt_vol;
	}

	@Override
	public String toString()
	{
		return String.format("SecurityDetail_Info5 {lt_price=%s, lt_open_price=%s, lt_vol=%s}", lt_price, lt_open_price, lt_vol);
	}
}