package db.tpce.gen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.ResultSetHandler;

import db.bench.DbRandom;

public class SymbolSelection
{
	private static final SymbolSelection instance = new SymbolSelection();

	private final List<String> lst_obj;
	private int l_size;

	private final DbRandom drand = new DbRandom();

	private SymbolSelection()
	{
		super();

		ResultSetHandler<String> rs_ex = new ResultSetHandler<String>()
		{
			@Override
			public String process(ResultSet rs)
			{
				try
				{
					return rs.getString("s_symb").trim();
				}
				catch (SQLException e)
				{
					throw new RuntimeException(e);
				}
			}
		};

		DbFetcher<String> dbt = new DbFetcher<String>();

		lst_obj = dbt.fetchList("SELECT DISTINCT s_symb FROM security;", rs_ex);
		l_size = lst_obj.size() - 1;
	}

	public static String getRandom()
	{
		int s = instance.l_size - 1;
		int r = instance.drand.rndIntRange(0, s);
		return instance.lst_obj.get(r);
	}
	
	public static String geNonUniformtRandom(int A, int S)
	{
		int s = instance.l_size - 1;
		int r = instance.drand.rndIntRange(0, s);
		return instance.lst_obj.get(r);
	}
}