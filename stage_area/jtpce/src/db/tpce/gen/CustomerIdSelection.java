package db.tpce.gen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.caudexorigo.Shutdown;
import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.ResultSetHandler;

import db.bench.DbRandom;
import db.tpce.common.CustomerId;

public class CustomerIdSelection
{
	private static final CustomerIdSelection instance = new CustomerIdSelection();

	private final List<CustomerId> lst_obj;
	private final int lst_size;

	private final DbRandom drand = new DbRandom();

	private CustomerIdSelection()
	{
		super();

		ResultSetHandler<CustomerId> rs_ex = new ResultSetHandler<CustomerId>()
		{
			@Override
			public CustomerId process(ResultSet rs)
			{
				try
				{
					return new CustomerId(rs.getLong("c_id"), rs.getString("c_tax_id"), rs.getLong("min_ca_id"), rs.getInt("count_ca"));
				}
				catch (SQLException e)
				{
					Shutdown.now(e);
					return null;
				}
			}
		};

		DbFetcher<CustomerId> dbt = new DbFetcher<CustomerId>();

		lst_obj = dbt.fetchList("SELECT c_id, c_tax_id, min(ca_id) AS min_ca_id, count(ca_id) AS count_ca FROM customer, customer_account WHERE customer.c_id = customer_account.ca_c_id GROUP BY c_id, c_tax_id;", rs_ex);
		lst_size = lst_obj.size();
	}

	public static CustomerId getRandom()
	{
		int s = instance.lst_size - 1;
		int r = instance.drand.rndIntRange(0, s);
		return instance.lst_obj.get(r);
	}

	public static CustomerId getNonUniformRandom()
	{
		long r = instance.drand.nonUniformRandom(1, instance.lst_size);
		int s = (int) r - 1;
		return instance.lst_obj.get(s);
	}
}