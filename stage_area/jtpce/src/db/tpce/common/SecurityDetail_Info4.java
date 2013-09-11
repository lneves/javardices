package db.tpce.common;

import java.math.BigDecimal;
import java.sql.Date;

public class SecurityDetail_Info4
{
	public final java.sql.Date dm_date;
	public final BigDecimal dm_close;
	public final BigDecimal dm_high;
	public final BigDecimal dm_low;
	public final long dm_vol;

	public SecurityDetail_Info4(Date dm_date, BigDecimal dm_close, BigDecimal dm_high, BigDecimal dm_low, long dm_vol)
	{
		super();
		this.dm_date = dm_date;
		this.dm_close = dm_close;
		this.dm_high = dm_high;
		this.dm_low = dm_low;
		this.dm_vol = dm_vol;
	}

	@Override
	public String toString()
	{
		return String.format("SecurityDetail_Info4 {dm_date=%s, dm_close=%s, dm_high=%s, dm_low=%s, dm_vol=%s}", dm_date, dm_close, dm_high, dm_low, dm_vol);
	}
}