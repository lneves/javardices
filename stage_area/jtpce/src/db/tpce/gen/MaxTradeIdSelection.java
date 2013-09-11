package db.tpce.gen;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.ResultSetHandler;

public class MaxTradeIdSelection
{
	private static final MaxTradeIdSelection instance = new MaxTradeIdSelection();

	private final long max_trade_id;

	private MaxTradeIdSelection()
	{
		super();

		ResultSetHandler<Long> rs_ex = new ResultSetHandler<Long>()
		{
			@Override
			public Long process(ResultSet rs)
			{
				try
				{
					return rs.getLong(1);
				}
				catch (SQLException e)
				{
					throw new RuntimeException(e);
				}
			}
		};

		DbFetcher<Long> db_runner = new DbFetcher<Long>();
		max_trade_id = db_runner.fetchObject("SELECT max(t_id) FROM trade", rs_ex);
	}

	public static long get()
	{
		return instance.max_trade_id;
	}

	public static long getUnshifted()
	{
		return (instance.max_trade_id - GenConstants.iTTradeShift);
	}
}