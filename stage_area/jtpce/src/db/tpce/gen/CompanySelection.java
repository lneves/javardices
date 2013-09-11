package db.tpce.gen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.caudexorigo.Shutdown;
import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.ResultSetHandler;

import db.bench.DbRandom;
import db.tpce.common.Company;

public class CompanySelection

{
	private static final CompanySelection instance = new CompanySelection();

	private final List<Company> lst_obj;
	private final int lst_size;

	private final DbRandom drand = new DbRandom();

	private CompanySelection()
	{
		super();

		ResultSetHandler<Company> rs_ex = new ResultSetHandler<Company>()
		{
			@Override
			public Company process(ResultSet rs)
			{
				try
				{
					return new Company(rs.getLong("co_id"));
				}
				catch (SQLException e)
				{
					Shutdown.now(e);
					return null;
				}
			}
		};

		DbFetcher<Company> dbt = new DbFetcher<Company>();

		lst_obj = dbt.fetchList("SELECT co_id FROM company ORDER BY co_id ASC;", rs_ex);
		lst_size = lst_obj.size();
	}

	public static int getCount()
	{
		return instance.lst_size;
	}

	public static Company getRandom()
	{
		int s = instance.lst_obj.size() - 1;
		int r = instance.drand.rndIntRange(0, s);
		return instance.lst_obj.get(r);
	}

	public static long get(int i)
	{
		return instance.lst_obj.get(i).co_id;
	}
}