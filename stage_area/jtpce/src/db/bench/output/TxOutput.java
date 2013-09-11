package db.bench.output;

public abstract class TxOutput
{
	public final int status;
	public long tx_time;
	
	public TxOutput(int status)
	{
		super();
		this.status = status;
	}
}