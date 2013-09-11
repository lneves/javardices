package db.tpce.input;

public class TCustomerPositionFrame2Input
{
	public long acct_id;

	public TCustomerPositionFrame2Input()
	{
		super();
	}

	@Override
	public String toString()
	{
		return String.format("TCustomerPositionFrame2Input [acct_id=%s]", acct_id);
	}
}