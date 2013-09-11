package db.bench.tx;

import db.bench.output.TxOutput;

public interface Tx
{
	public String name();
	
	public TxOutput execute();
}