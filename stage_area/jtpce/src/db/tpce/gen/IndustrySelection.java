package db.tpce.gen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.ResultSetHandler;

import db.bench.DbRandom;

public class IndustrySelection
{
	private static final IndustrySelection instance = new IndustrySelection();

	private final List<String> lst_obj;

	private final DbRandom drand = new DbRandom();

	private IndustrySelection()
	{
		super();

		ResultSetHandler<String> rs_ex = new ResultSetHandler<String>()
		{
			@Override
			public String process(ResultSet rs)
			{
				try
				{
					return rs.getString("in_name");
				}
				catch (SQLException e)
				{
					throw new RuntimeException(e);
				}
			}
		};

		DbFetcher<String> dbt = new DbFetcher<String>();

		lst_obj = dbt.fetchList("SELECT DISTINCT in_name FROM industry;", rs_ex);
	}

	public static String getRandom()
	{
		int s = instance.lst_obj.size() - 1;
		int r = instance.drand.rndIntRange(0, s);
		return instance.lst_obj.get(r);
	}
}