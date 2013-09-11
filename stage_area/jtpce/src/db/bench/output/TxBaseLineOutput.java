package db.bench.output;

public class TxBaseLineOutput extends TxOutput
{
	public TxBaseLineOutput(int status)
	{
		super(status);
	}

	@Override
	public String toString()
	{
		return String.format("TxBaseLineOutput [status=%s, tx_time=%s]", status, tx_time);
	}
}