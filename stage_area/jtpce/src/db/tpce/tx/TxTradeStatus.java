package db.tpce.tx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.DbPool;
import org.caudexorigo.jdbc.DbType;
import org.caudexorigo.jdbc.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.bench.Dml;
import db.bench.output.TxOutput;
import db.bench.tx.TxBase;
import db.tpce.common.TradeInfo;
import db.tpce.gen.CEInputGeneration;
import db.tpce.input.TTradeStatusInput;
import db.tpce.output.TTradeStatusFrame1Output;

public class TxTradeStatus extends TxBase
{
	private static final Logger log = LoggerFactory.getLogger(TxTradeStatus.class);

	private static final String ts_f1_sql;

	private static final ResultSetHandler<TradeInfo> rs_ex = new ResultSetHandler<TradeInfo>()
	{
		@Override
		public TradeInfo process(ResultSet rs)
		{
			try
			{
				return new TradeInfo(rs.getString("cust_l_name"), rs.getString("cust_f_name"), rs.getString("broker_name"), rs.getBigDecimal("charge"), rs.getString("exec_name"), rs.getString("ex_name"), rs.getString("s_name"), rs.getString("status_name"), rs.getString("symbol"), rs.getTimestamp("trade_dts"), rs.getLong("trade_id"), rs.getInt("trade_qty"), rs.getString("type_name"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	static
	{
		if (DbPool.getDbType() == DbType.MSSQL)
		{
			ts_f1_sql = Dml.load("trade_status_frm1_ms.sql");
		}
		else if (DbPool.getDbType() == DbType.MYSQL)
		{
			ts_f1_sql = Dml.load("trade_status_frm1_my.sql");
		}
		else
		{
			ts_f1_sql = Dml.load("trade_status_frm1.sql");
		}
	}

	private final CEInputGeneration gen;
	private final String tx_name;

	public TxTradeStatus(CEInputGeneration gen)
	{
		super();
		this.gen = gen;
		tx_name = "Trade-Status";
	}

	@Override
	protected final TxOutput run()
	{
		int status = 0;

		final TTradeStatusInput frm1_input = gen.generateTradeStatusInput();

		if (log.isDebugEnabled())
		{
			log.debug(frm1_input.toString());
		}

		DbFetcher<TradeInfo> db = new DbFetcher<TradeInfo>();

		List<TradeInfo> lst_obj;

		lst_obj = db.fetchList(ts_f1_sql, rs_ex, frm1_input.acct_id, frm1_input.acct_id);

		if (lst_obj.size() != 50)
		{
			status = -911;
		}

		TTradeStatusFrame1Output frm1_output = new TTradeStatusFrame1Output(status, lst_obj);

		return frm1_output;
	}

	@Override
	public String name()
	{
		return tx_name;
	}
}