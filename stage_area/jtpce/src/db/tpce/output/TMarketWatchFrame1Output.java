package db.tpce.output;

import db.bench.output.TxOutput;

public class TMarketWatchFrame1Output extends TxOutput
{
	public final double pct_change;

	public TMarketWatchFrame1Output(int status, double pct_change)
	{
		super(status);
		this.pct_change = pct_change;
	}

	@Override
	public String toString()
	{
		return String.format("TMarketWatchFrame1Output [%npct_change=%s%n, status=%s%n, tx_time=%s%n]", pct_change, status, ((double) tx_time / 1000000.0));
	}
}