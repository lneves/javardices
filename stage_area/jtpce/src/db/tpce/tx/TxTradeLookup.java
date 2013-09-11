package db.tpce.tx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.caudexorigo.jdbc.Db;
import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.DbPool;
import org.caudexorigo.jdbc.DbType;
import org.caudexorigo.jdbc.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.bench.Dml;
import db.bench.output.TxOutput;
import db.bench.tx.TxBase;
import db.tpce.common.TradeLookup0;
import db.tpce.common.TradeLookup1;
import db.tpce.common.TradeLookup2;
import db.tpce.common.TradeLookup3;
import db.tpce.common.TradeLookup4;
import db.tpce.common.TradeLookupHistory;
import db.tpce.gen.CEInputGeneration;
import db.tpce.input.TTradeLookupInput;
import db.tpce.output.TTradeLookupOutput;

public class TxTradeLookup extends TxBase
{
	private static final Logger log = LoggerFactory.getLogger(TxTradeLookup.class);

	// Based on the maximum number of status changes a trade can go through.
	public static final int TradeLookupMaxTradeHistoryRowsReturned = 3;

	// Max number of rows for the frames
	public static final int TradeLookupMaxRows = 20;

	public static final int TradeLookupFrame1MaxRows = TradeLookupMaxRows;
	public static final int TradeLookupFrame2MaxRows = TradeLookupMaxRows;
	public static final int TradeLookupFrame3MaxRows = TradeLookupMaxRows;
	public static final int TradeLookupFrame4MaxRows = TradeLookupMaxRows;

	private static final String tl_f1_sql;
	private static final String tl_f2_sql;
	private static final String tl_f3_sql;
	private static final String tl_f4_sql;
	private static final String tl_hist_sql;


	static
	{
		tl_hist_sql = Dml.load("trade_lookup_history.sql");


		if (DbPool.getDbType() == DbType.MSSQL)
		{
			tl_f1_sql = Dml.load("trade_lookup_frm1.sql");
			tl_f2_sql = Dml.load("trade_lookup_frm2_ms.sql");
			tl_f3_sql = Dml.load("trade_lookup_frm3_ms.sql");
			tl_f4_sql = Dml.load("trade_lookup_frm4_ms.sql");
		}
		else if (DbPool.getDbType() == DbType.MYSQL)
		{
			tl_f1_sql = Dml.load("trade_lookup_frm1_my.sql");
			tl_f2_sql = Dml.load("trade_lookup_frm2.sql");
			tl_f3_sql = Dml.load("trade_lookup_frm3.sql");
			tl_f4_sql = Dml.load("trade_lookup_frm4_my.sql");
		}
		else
		{
			tl_f1_sql = Dml.load("trade_lookup_frm1.sql");
			tl_f2_sql = Dml.load("trade_lookup_frm2.sql");
			tl_f3_sql = Dml.load("trade_lookup_frm3.sql");
			tl_f4_sql = Dml.load("trade_lookup_frm4.sql");
		}
	}

	private static final ResultSetHandler<TradeLookupHistory> rs_ex_hist = new ResultSetHandler<TradeLookupHistory>()
	{
		@Override
		public TradeLookupHistory process(ResultSet rs)
		{
			try
			{
				return new TradeLookupHistory(rs.getTimestamp("th_dts"), rs.getString("th_st_id"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<TradeLookup1> tl_f1_rs_ex = new ResultSetHandler<TradeLookup1>()
	{
		@Override
		public TradeLookup1 process(ResultSet rs)
		{
			try
			{
				return new TradeLookup1(rs.getLong("t_id"), rs.getBigDecimal("t_bid_price"), rs.getString("t_exec_name"), rs.getBoolean("t_is_cash"), rs.getBoolean("tt_is_mrkt"), rs.getBigDecimal("t_trade_price"), rs.getBigDecimal("se_amt"), rs.getDate("se_cash_due_date"), rs.getString("se_cash_type"), rs.getBigDecimal("ct_amt"), rs.getString("ct_name"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<TradeLookup2> tl_f2_rs_ex = new ResultSetHandler<TradeLookup2>()
	{
		@Override
		public TradeLookup2 process(ResultSet rs)
		{
			try
			{

				return new TradeLookup2(rs.getLong("t_id"), rs.getString("t_exec_name"), rs.getBigDecimal("t_bid_price"), rs.getBigDecimal("t_trade_price"), rs.getBoolean("t_is_cash"), rs.getBigDecimal("se_amt"), rs.getDate("se_cash_due_date"), rs.getString("se_cash_type"), rs.getBigDecimal("ct_amt"), rs.getString("ct_name"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<TradeLookup3> tl_f3_rs_ex = new ResultSetHandler<TradeLookup3>()
	{
		@Override
		public TradeLookup3 process(ResultSet rs)
		{
			try
			{
				return new TradeLookup3(rs.getLong("t_id"), rs.getLong("t_ca_id"), rs.getString("t_exec_name"), rs.getBoolean("t_is_cash"), rs.getBigDecimal("t_trade_price"), rs.getInt("t_qty"), rs.getTimestamp("t_dts"), rs.getString("t_tt_id"), rs.getBigDecimal("se_amt"), rs.getDate("se_cash_due_date"), rs.getString("se_cash_type"), rs.getBigDecimal("ct_amt"), rs.getString("ct_name"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<TradeLookup4> tl_f4_rs_ex = new ResultSetHandler<TradeLookup4>()
	{
		@Override
		public TradeLookup4 process(ResultSet rs)
		{
			try
			{
				return new TradeLookup4(rs.getLong("hh_h_t_id"), rs.getLong("hh_t_id"), rs.getInt("hh_before_qty"), rs.getInt("hh_after_qty"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private final CEInputGeneration gen;
	private final String tx_name;

	public TxTradeLookup(CEInputGeneration gen)
	{
		super();
		this.gen = gen;
		tx_name = "Trade-Lookup";
	}

	@Override
	public String name()
	{
		return tx_name;
	}

	@Override
	protected TxOutput run()
	{
		int status = 0;

		final TTradeLookupInput frm_input = gen.generateTradeLookupInput();

		if (log.isDebugEnabled())
		{
			log.debug(frm_input.toString());
		}

		TTradeLookupOutput out = null;

		Db db = null;
		try
		{
			db = DbPool.obtain();

			if (frm_input.frame_to_execute == 1)
			{
				Object[] tid_args = new Object[20];

				for (int i = 0; i < tid_args.length; i++)
				{
					tid_args[i] = frm_input.trade_id[i];

				}

				DbFetcher<TradeLookup1> dbrunner = new DbFetcher<TradeLookup1>();

				long start = System.nanoTime();

				List<TradeLookup1> lstTrades = dbrunner.fetchList(db, tl_f1_sql, tl_f1_rs_ex, tid_args);

				double elapsed = ((double) Math.abs(System.nanoTime() - start)) / 1000000.0;

				System.out.printf("TxTradeLookup.frame1: %.2f ms.%n", elapsed);

				List<TradeLookupHistory> lstTradesHistory = getTradeHistory(db, lstTrades);

				if (lstTrades.size() != 20)
				{
					status = -611;
				}

				out = new TTradeLookupOutput(status, 1, lstTrades, null, null, null, lstTradesHistory);
			}
			else if (frm_input.frame_to_execute == 2)
			{
				DbFetcher<TradeLookup2> dbrunner = new DbFetcher<TradeLookup2>();

				long start = System.nanoTime();

				List<TradeLookup2> lstTrades = dbrunner.fetchList(db, tl_f2_sql, tl_f2_rs_ex, frm_input.acct_id, frm_input.start_trade_dts, frm_input.end_trade_dts);

				double elapsed = ((double) Math.abs(System.nanoTime() - start)) / 1000000.0;

				System.out.printf("TxTradeLookup.frame2: %.2f ms.%n", elapsed);

				List<TradeLookupHistory> lstTradesHistory = getTradeHistory(db, lstTrades);

				if (lstTrades.size() < 0)
				{
					status = -621;
				}
				if (lstTrades.size() == 0)
				{
					status = +621;
				}

				out = new TTradeLookupOutput(status, 2, null, lstTrades, null, null, lstTradesHistory);
			}
			else if (frm_input.frame_to_execute == 3)
			{

				DbFetcher<TradeLookup3> dbrunner = new DbFetcher<TradeLookup3>();

				long start = System.nanoTime();
				List<TradeLookup3> lstTrades = dbrunner.fetchList(db, tl_f3_sql, tl_f3_rs_ex, frm_input.symbol, frm_input.start_trade_dts, frm_input.end_trade_dts);

				double elapsed = ((double) Math.abs(System.nanoTime() - start)) / 1000000.0;

				System.out.printf("TxTradeLookup.frame3: %.2f ms.%n", elapsed);

				List<TradeLookupHistory> lstTradesHistory = getTradeHistory(db, lstTrades);

				if (lstTrades.size() < 0)
				{
					status = -621;
				}
				if (lstTrades.size() == 0)
				{
					status = +21;
				}

				out = new TTradeLookupOutput(status, 3, null, null, lstTrades, null, lstTradesHistory);
			}
			else
			{
				assert (frm_input.frame_to_execute == 4);

				DbFetcher<TradeLookup4> dbrunner = new DbFetcher<TradeLookup4>();

				long start = System.nanoTime();

				List<TradeLookup4> lstTradesFrm4 = dbrunner.fetchList(db, tl_f4_sql, tl_f4_rs_ex, frm_input.acct_id, frm_input.start_trade_dts);

				double elapsed = ((double) Math.abs(System.nanoTime() - start)) / 1000000.0;

				System.out.printf("TxTradeLookup.frame4: %.2f ms.%n", elapsed);

				out = new TTradeLookupOutput(status, 4, null, null, null, lstTradesFrm4, null);
			}

			return out;

		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			DbPool.release(db);
		}
	}

	private List<TradeLookupHistory> getTradeHistory(Db db, List<? extends TradeLookup0> lstTrades)
	{

		long start = System.nanoTime();

		Object[] tid_args = new Object[20];
		int ix = 0;

		for (TradeLookup0 tradeLookup : lstTrades)
		{
			tid_args[ix++] = tradeLookup.getTid();
		}

		DbFetcher<TradeLookupHistory> dbrunner_hist = new DbFetcher<TradeLookupHistory>();

		List<TradeLookupHistory> lstTradesHistory = dbrunner_hist.fetchList(db, tl_hist_sql, rs_ex_hist, tid_args);

		double elapsed = ((double) Math.abs(System.nanoTime() - start)) / 1000000.0;

		System.out.printf("TxTradeLookup.getTradeHistory: %.2f ms.%n", elapsed);

		return lstTradesHistory;
	}
}