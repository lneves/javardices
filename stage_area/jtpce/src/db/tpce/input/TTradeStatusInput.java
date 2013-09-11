package db.tpce.input;

public class TTradeStatusInput
{
	public final long acct_id;

	public TTradeStatusInput(long acct_id)
	{
		super();
		this.acct_id = acct_id;
	}

	@Override
	public String toString()
	{
		return String.format("TTradeStatusInput [acct_id=%s]", acct_id);
	}
}