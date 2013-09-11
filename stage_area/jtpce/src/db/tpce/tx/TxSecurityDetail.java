package db.tpce.tx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
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
import db.tpce.common.SecurityDetail_Info1;
import db.tpce.common.SecurityDetail_Info2;
import db.tpce.common.SecurityDetail_Info3;
import db.tpce.common.SecurityDetail_Info4;
import db.tpce.common.SecurityDetail_Info5;
import db.tpce.common.SecurityDetail_Info6;
import db.tpce.common.SecurityDetail_Info7;
import db.tpce.gen.CEInputGeneration;
import db.tpce.input.TSecurityDetailInput;
import db.tpce.output.TSecurityDetailOutput;

public class TxSecurityDetail extends TxBase
{
	private static final Logger log = LoggerFactory.getLogger(TxSecurityDetail.class);

	private static final int min_day_len = 5;
	private static final int max_day_len = 20;
	private static final int max_fin_len = 20;
	private static final int max_news_len = 2;
	private static final int max_comp_len = 3;

	private static final String sd_frm1_1_sql;
	private static final String sd_frm1_2_sql;
	private static final String sd_frm1_3_sql;
	private static final String sd_frm1_4_sql;
	private static final String sd_frm1_5_sql;
	private static final String sd_frm1_6_sql;
	private static final String sd_frm1_7_sql;

	static
	{
		sd_frm1_1_sql = Dml.load("security_detail_frm1_info1.sql");
		sd_frm1_2_sql = Dml.load("security_detail_frm1_info2.sql");
		sd_frm1_5_sql = Dml.load("security_detail_frm1_info5.sql");

		if (DbPool.getDbType() == DbType.MSSQL)
		{
			sd_frm1_3_sql = Dml.load("security_detail_frm1_info3_ms.sql");
			sd_frm1_4_sql = Dml.load("security_detail_frm1_info4_ms.sql");
			sd_frm1_6_sql = Dml.load("security_detail_frm1_info6_ms.sql");
			sd_frm1_7_sql = Dml.load("security_detail_frm1_info7_ms.sql");
		}
		else
		{
			sd_frm1_3_sql = Dml.load("security_detail_frm1_info3.sql");
			sd_frm1_4_sql = Dml.load("security_detail_frm1_info4.sql");
			sd_frm1_6_sql = Dml.load("security_detail_frm1_info6.sql");
			sd_frm1_7_sql = Dml.load("security_detail_frm1_info7.sql");
		}
	}

	private static final ResultSetHandler<SecurityDetail_Info1> rs_ex_1 = new ResultSetHandler<SecurityDetail_Info1>()
	{
		@Override
		public SecurityDetail_Info1 process(ResultSet rs)
		{
			try
			{
				return new SecurityDetail_Info1(rs.getString("s_name"), rs.getLong("co_id"), rs.getString("co_name"), rs.getString("co_sp_rate"), rs.getString("co_ceo"), rs.getString("co_desc"), rs.getDate("co_open_date"), rs.getString("co_st_id"), rs.getString("co_ad_line1"), rs.getString("co_ad_line2"), rs.getString("co_zc_town"), rs.getString("co_zc_div"), rs.getString("co_ad_zc_code"), rs.getString("co_ad_ctry"), rs.getLong("s_num_out"), rs.getDate("s_start_date"), rs.getDate("s_exch_date"), rs.getBigDecimal("s_pe"), rs.getBigDecimal("s_52wk_high"), rs.getDate("s_52wk_high_date"), rs.getBigDecimal("s_52wk_low"), rs.getDate("s_52wk_low_date"), rs.getBigDecimal("s_dividend"), rs.getBigDecimal("s_yield"), rs.getString("ex_ad_line1"), rs.getString("ex_ad_line2"),
						rs.getString("ex_zc_town"), rs.getString("ex_zc_div"), rs.getString("ex_ad_ctry"), rs.getString("ex_ad_zc_code"), rs.getString("ex_desc"), rs.getString("ex_name"), rs.getInt("ex_num_symb"), rs.getInt("ex_open"), rs.getInt("ex_close"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<SecurityDetail_Info2> rs_ex_2 = new ResultSetHandler<SecurityDetail_Info2>()
	{
		@Override
		public SecurityDetail_Info2 process(ResultSet rs)
		{
			try
			{
				return new SecurityDetail_Info2(rs.getString("co_name"), rs.getString("in_name"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<SecurityDetail_Info3> rs_ex_3 = new ResultSetHandler<SecurityDetail_Info3>()
	{
		@Override
		public SecurityDetail_Info3 process(ResultSet rs)
		{
			try
			{
				return new SecurityDetail_Info3(rs.getInt("fi_year"), rs.getInt("fi_qtr"), rs.getDate("fi_qtr_start_date"), rs.getBigDecimal("fi_revenue"), rs.getBigDecimal("fi_net_earn"), rs.getBigDecimal("fi_basic_eps"), rs.getBigDecimal("fi_dilut_eps"), rs.getBigDecimal("fi_margin"), rs.getBigDecimal("fi_inventory"), rs.getBigDecimal("fi_assets"), rs.getBigDecimal("fi_liability"), rs.getLong("fi_out_basic"), rs.getLong("fi_out_dilut"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<SecurityDetail_Info4> rs_ex_4 = new ResultSetHandler<SecurityDetail_Info4>()
	{
		@Override
		public SecurityDetail_Info4 process(ResultSet rs)
		{
			try
			{
				return new SecurityDetail_Info4(rs.getDate("dm_date"), rs.getBigDecimal("dm_close"), rs.getBigDecimal("dm_high"), rs.getBigDecimal("dm_low"), rs.getLong("dm_vol"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<SecurityDetail_Info5> rs_ex_5 = new ResultSetHandler<SecurityDetail_Info5>()
	{
		@Override
		public SecurityDetail_Info5 process(ResultSet rs)
		{
			try
			{
				return new SecurityDetail_Info5(rs.getBigDecimal("lt_price"), rs.getBigDecimal("lt_open_price"), rs.getLong("lt_vol"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<SecurityDetail_Info6> rs_ex_6 = new ResultSetHandler<SecurityDetail_Info6>()
	{
		@Override
		public SecurityDetail_Info6 process(ResultSet rs)
		{
			try
			{
				return new SecurityDetail_Info6(rs.getDate("ni_dts"), rs.getString("ni_source"), rs.getString("ni_author"), new String(rs.getBytes("ni_item")));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<SecurityDetail_Info7> rs_ex_7 = new ResultSetHandler<SecurityDetail_Info7>()
	{
		@Override
		public SecurityDetail_Info7 process(ResultSet rs)
		{
			try
			{
				return new SecurityDetail_Info7(rs.getDate("ni_dts"), rs.getString("ni_source"), rs.getString("ni_author"), rs.getString("ni_headline"), rs.getString("ni_summary"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private final CEInputGeneration gen;
	private final String tx_name;

	public TxSecurityDetail(CEInputGeneration gen)
	{
		super();
		this.gen = gen;
		tx_name = "Security-Detail";
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

		final TSecurityDetailInput frm1_input = gen.generateSecurityDetailInput();

		if (log.isDebugEnabled())
		{
			log.debug(frm1_input.toString());
		}

		Db db = null;
		try
		{
			db = DbPool.obtain();
			db.beginTransaction();

			DbFetcher<SecurityDetail_Info1> dbrunner1 = new DbFetcher<SecurityDetail_Info1>();
			DbFetcher<SecurityDetail_Info2> dbrunner2 = new DbFetcher<SecurityDetail_Info2>();
			DbFetcher<SecurityDetail_Info3> dbrunner3 = new DbFetcher<SecurityDetail_Info3>();
			DbFetcher<SecurityDetail_Info4> dbrunner4 = new DbFetcher<SecurityDetail_Info4>();
			DbFetcher<SecurityDetail_Info5> dbrunner5 = new DbFetcher<SecurityDetail_Info5>();
			DbFetcher<SecurityDetail_Info6> dbrunner6 = new DbFetcher<SecurityDetail_Info6>();
			DbFetcher<SecurityDetail_Info7> dbrunner7 = new DbFetcher<SecurityDetail_Info7>();

			SecurityDetail_Info1 sd_info_1 = dbrunner1.fetchObject(db, sd_frm1_1_sql, rs_ex_1, frm1_input.symbol);

			List<SecurityDetail_Info2> lst_sd_info_2 = dbrunner2.fetchList(db, sd_frm1_2_sql, rs_ex_2, sd_info_1.co_id);

			List<SecurityDetail_Info3> lst_sd_info_3 = dbrunner3.fetchList(db, sd_frm1_3_sql, rs_ex_3, sd_info_1.co_id);

			List<SecurityDetail_Info4> lst_sd_info_4 = dbrunner4.fetchList(db, String.format(sd_frm1_4_sql, frm1_input.max_rows_to_return), rs_ex_4, frm1_input.symbol, frm1_input.start_day);

			SecurityDetail_Info5 sd_info_5 = dbrunner5.fetchObject(db, sd_frm1_5_sql, rs_ex_5, frm1_input.symbol);

			List<SecurityDetail_Info6> lst_sd_info_6;
			List<SecurityDetail_Info7> lst_sd_info_7;

			if (frm1_input.access_lob_flag)
			{
				lst_sd_info_6 = dbrunner6.fetchList(db, sd_frm1_6_sql, rs_ex_6, sd_info_1.co_id);
				lst_sd_info_7 = Collections.emptyList();
			}
			else
			{
				lst_sd_info_6 = Collections.emptyList();
				lst_sd_info_7 = dbrunner7.fetchList(db, sd_frm1_7_sql, rs_ex_7, sd_info_1.co_id);
			}

			db.commitTransaction();

			int day_len = lst_sd_info_4.size();
			int fin_len = lst_sd_info_3.size();
			int news_len = lst_sd_info_6.size() + lst_sd_info_7.size();

			if ((day_len < min_day_len) || (day_len > max_day_len))
			{
				status = -511;
			}
			else if (fin_len != max_fin_len)
			{
				status = -512;
			}
			else if (news_len != max_news_len)
			{
				status = -513;
			}

			TSecurityDetailOutput out = new TSecurityDetailOutput(status, sd_info_1, lst_sd_info_2, lst_sd_info_3, lst_sd_info_4, sd_info_5, lst_sd_info_6, lst_sd_info_7);

			return out;
		}
		catch (Throwable ex)
		{
			db.rollbackTransaction();
			throw new RuntimeException(ex);
		}
		finally
		{
			DbPool.release(db);
		}
	}
}