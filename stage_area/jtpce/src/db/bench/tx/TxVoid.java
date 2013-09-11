package db.bench.tx;

import db.bench.output.TxOutput;

public class TxVoid extends TxBase
{
	private final String name;

	private static TxOutput null_output = new TxOutput(0)
	{
	};

	public TxVoid(String name)
	{
		super();
		this.name = name;
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	protected TxOutput run()
	{
		return null_output;
	}
}