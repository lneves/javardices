package db.tpce.input;

public class TTradeStatusFrame1Input
{
	public final long acct_id;

	public TTradeStatusFrame1Input(long acct_id)
	{
		super();
		this.acct_id = acct_id;
	}

	@Override
	public String toString()
	{
		return String.format("TTradeStatusFrame1Input [acct_id=%s]", acct_id);
	}
}