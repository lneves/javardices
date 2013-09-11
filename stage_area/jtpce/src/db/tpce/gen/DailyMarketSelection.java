package db.tpce.gen;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.ResultSetHandler;

import db.bench.DbRandom;

public class DailyMarketSelection
{
	private static final DailyMarketSelection instance = new DailyMarketSelection();

	private final List<Date> lst_obj;
	private final int l_size;

	private final DbRandom drand = new DbRandom();

	private DailyMarketSelection()
	{
		super();

		ResultSetHandler<Date> rs_ex = new ResultSetHandler<Date>()
		{
			@Override
			public Date process(ResultSet rs)
			{
				try
				{
					return rs.getDate("dm_date");
				}
				catch (SQLException e)
				{
					throw new RuntimeException(e);
				}
			}
		};

		DbFetcher<Date> dbt = new DbFetcher<Date>();

		lst_obj = dbt.fetchList("SELECT DISTINCT dm_date FROM daily_market;", rs_ex);
		l_size = lst_obj.size();
	}

	public static Date getRandom()
	{
		int s = instance.l_size - 1;
		int r = instance.drand.rndIntRange(0, s);
		return instance.lst_obj.get(r);
	}
	
	public static int getRandomIndex()
	{
		int s = instance.l_size - 1;
		int r = instance.drand.rndIntRange(0, s);
		return  r;
	}
	
	public static Date getByIndex(int ix)
	{
		int r = Math.max(0, ix);
		return instance.lst_obj.get(r);		
	}

	public static Date getNonUniformRandom()
	{
		long r = instance.drand.nonUniformRandom(1, instance.l_size);
		int s = (int) r - 1;
		return instance.lst_obj.get(s);
	}
}