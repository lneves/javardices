package db.tpce.tx;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.ResultSetHandler;
import org.caudexorigo.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.bench.Dml;
import db.bench.output.TxOutput;
import db.bench.tx.TxBase;
import db.tpce.gen.CEInputGeneration;
import db.tpce.input.TMarketWatchInput;
import db.tpce.output.TMarketWatchFrame1Output;

public class TxMarketWatch extends TxBase
{
	private static final Logger log = LoggerFactory.getLogger(TxMarketWatch.class);

	private static final String mw_f1_by_customer_sql;

	private static final String mw_f1_by_account_sql;

	private static final String mw_f1_by_industry_sql;

	static
	{
		mw_f1_by_customer_sql = Dml.load("market_watch_frm1_by_customer.sql");
		mw_f1_by_account_sql = Dml.load("market_watch_frm1_by_account.sql");
		mw_f1_by_industry_sql = Dml.load("market_watch_frm1_by_industry.sql");
	}

	private static final ResultSetHandler<BigDecimal> rs_f1_get_numeric = new ResultSetHandler<BigDecimal>()
	{
		@Override
		public BigDecimal process(ResultSet rs)
		{
			try
			{
				return rs.getBigDecimal(1);
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private final CEInputGeneration gen;
	private final String tx_name;

	public TxMarketWatch(CEInputGeneration gen)
	{
		super();
		this.gen = gen;
		tx_name = "Market-Watch";
	}

	@Override
	public String name()
	{
		return tx_name;
	}

	@Override
	protected final TxOutput run()
	{
		final TMarketWatchInput frm1_input = gen.generateMarketWatchInput();

		if (log.isDebugEnabled())
		{
			log.debug(frm1_input.toString());
		}

		long c_id = frm1_input.c_id;
		long acct_id = frm1_input.acct_id;
		long starting_co_id = frm1_input.starting_co_id;
		long ending_co_id = frm1_input.ending_co_id;
		String industry_name = frm1_input.industry_name;
		java.sql.Date start_date = frm1_input.start_date;

		int status = 0;
		double d_pct_change = 0.0;

		if ((acct_id != 0) || (c_id != 0) || (StringUtils.isNotBlank(industry_name)))
		{
			DbFetcher<BigDecimal> db = new DbFetcher<BigDecimal>();

			BigDecimal pct_change = null;

			if (c_id != 0)
			{
				pct_change = db.fetchObject(mw_f1_by_customer_sql, rs_f1_get_numeric, c_id, start_date);
			}
			else if (StringUtils.isNotBlank(industry_name))
			{
				pct_change = db.fetchObject(mw_f1_by_industry_sql, rs_f1_get_numeric, industry_name, starting_co_id, ending_co_id, start_date);
			}
			else if (acct_id != 0)
			{
				pct_change = db.fetchObject(mw_f1_by_account_sql, rs_f1_get_numeric, acct_id, start_date);
			}
			else
			{
				status = -1;
			}

			if (pct_change != null)
			{
				int ix = (int) (pct_change.doubleValue() * 1000.0);
				d_pct_change = ((double) ix) / 1000.0;
			}
		}
		else
		{
			status = -411;
		}

		return new TMarketWatchFrame1Output(status, d_pct_change);
	}
}