package db.tpce.gen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.ResultSetHandler;

import db.bench.DbRandom;
import db.tpce.common.Broker;

public class BrokerSelection
{
	private static final BrokerSelection instance = new BrokerSelection();

	private final List<Broker> lst_broker;

	private final DbRandom drand = new DbRandom();

	private BrokerSelection()
	{
		super();

		ResultSetHandler<Broker> rs_ex = new ResultSetHandler<Broker>()
		{
			@Override
			public Broker process(ResultSet rs)
			{
				long b_id;
				String b_st_id = null;
				String b_name = null;
				try
				{
					b_id = rs.getLong("b_id");
					b_st_id = rs.getString("b_st_id");
					b_name = rs.getString("b_name");
				}
				catch (SQLException e)
				{
					throw new RuntimeException(e);
				}

				return new Broker(b_id, b_st_id, b_name);
			}
		};

		DbFetcher<Broker> dbt = new DbFetcher<Broker>();

		lst_broker = dbt.fetchList("SELECT b_id, b_st_id, b_name FROM broker;", rs_ex);
	}

	public static Broker getRandom()
	{
		int s = instance.lst_broker.size() - 1;
		int r = instance.drand.rndIntRange(0, s);
		return instance.lst_broker.get(r);
	}
}