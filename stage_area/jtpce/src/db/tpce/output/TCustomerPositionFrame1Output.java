package db.tpce.output;

import java.util.List;

import db.bench.output.TxOutput;
import db.tpce.common.Customer;
import db.tpce.common.CustomerAsset;

public class TCustomerPositionFrame1Output extends TxOutput
{
	public final Customer customer;
	public final List<CustomerAsset> lst_asset;
	public final int acct_len;

	public TCustomerPositionFrame1Output(int status, Customer customer, List<CustomerAsset> lst_asset)
	{
		super(status);
		this.customer = customer;
		this.lst_asset = lst_asset;
		acct_len = lst_asset.size();
	}

	@Override
	public String toString()
	{
		return String.format("TCustomerPositionFrame1Output [%ncustomer=%s, lst_asset=%s%n, acct_len=%s%n, status=%s%n, tx_time=%s%n]", customer, lst_asset, acct_len, status, ((double) tx_time / 1000000.0));
	}
}