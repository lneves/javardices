package db.tpce.common;

import java.math.BigDecimal;

public class CustomerAsset
{

	public final long acct_id;
	public final BigDecimal cash_bal;
	public final BigDecimal assets_total;

	public CustomerAsset(long acct_id, BigDecimal cash_bal, BigDecimal assets_total)
	{
		super();
		this.acct_id = acct_id;
		this.cash_bal = cash_bal;
		this.assets_total = assets_total;
	}

	@Override
	public String toString()
	{
		return String.format("CustomerAsset {acct_id=%s, cash_bal=%s, assets_total=%s}", acct_id, cash_bal, assets_total);
	}
}