package db.bench.tx;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.ResultSetHandler;

import db.bench.output.TxBaseLineOutput;
import db.bench.output.TxOutput;

public class TxBaseLine extends TxBase
{

	private static final ResultSetHandler<Integer> rs_ex = new ResultSetHandler<Integer>()
	{
		@Override
		public Integer process(ResultSet rs)
		{
			try
			{
				return rs.getInt(1);
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	@Override
	public String name()
	{
		return "TxBaseLine";
	}

	@Override
	protected TxOutput run()
	{
		DbFetcher<Integer> db = new DbFetcher<Integer>();

		Integer ret = db.fetchObject("SELECT 1", rs_ex);

		return new TxBaseLineOutput(ret.intValue());
	}
}