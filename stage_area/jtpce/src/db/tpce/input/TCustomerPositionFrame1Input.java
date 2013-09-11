package db.tpce.input;

public class TCustomerPositionFrame1Input
{
	public int acct_id_idx;
	public long cust_id;
	public boolean get_history;
	public String tax_id;

	public TCustomerPositionFrame1Input()
	{
		super();
	}

	@Override
	public String toString()
	{
		return String.format("TCustomerPositionFrame1Input [%nacct_id_idx=%s%n, cust_id=%s%n, get_history=%s%n, tax_id=%s%n]", acct_id_idx, cust_id, get_history, tax_id);
	}
}