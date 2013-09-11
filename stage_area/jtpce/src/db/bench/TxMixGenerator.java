package db.bench;

import db.bench.tx.Tx;

public interface TxMixGenerator
{
	public Tx nextTransaction();

	public Tx txByName(String tx_name);

	public String[] transactionNames();

	public Profile profile();
}