package db.bench;

import db.bench.tx.Tx;
import db.bench.tx.TxBaseLine;

public class BaseLineMixGenerator implements TxMixGenerator
{
	private final Tx baseline_tx = new TxBaseLine();

	private final String[] tx_arr = { baseline_tx.name() };

	public BaseLineMixGenerator()
	{
		super();
	}

	@Override
	public Profile profile()
	{
		return Profile.READ_ONLY;
	}

	@Override
	public Tx nextTransaction()
	{
		return baseline_tx;
	}

	@Override
	public Tx txByName(String tx_name)
	{
		return baseline_tx;
	}

	@Override
	public String[] transactionNames()
	{
		return tx_arr;
	}
}