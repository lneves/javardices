package db.tpce.tx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.DbPool;
import org.caudexorigo.jdbc.DbType;
import org.caudexorigo.jdbc.ResultSetHandler;
import org.caudexorigo.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.bench.Dml;
import db.bench.output.TxOutput;
import db.bench.tx.TxBase;
import db.tpce.common.Customer;
import db.tpce.common.CustomerAsset;
import db.tpce.common.TradeHistory;
import db.tpce.gen.CEInputGeneration;
import db.tpce.input.TCustomerPositionFrame1Input;
import db.tpce.input.TCustomerPositionFrame2Input;
import db.tpce.output.TCustomerPositionFrame1Output;
import db.tpce.output.TCustomerPositionFrame2Output;

public class TxCustomerPosition extends TxBase
{
	private static final Logger log = LoggerFactory.getLogger(TxCustomerPosition.class);

	private static final int max_acct_len = 10;
	private static final int max_hist_len = 30;

	private static final String cp_f1_get_customer_by_cid_sql;
	private static final String cp_f1_get_customer_by_taxid_sql;
	private static final String cp_f1_get_assets_sql;
	private static final String cp_f1_get_trade_history_sql;

	static
	{
		cp_f1_get_customer_by_cid_sql = Dml.load("customer_position_frm1_get_customer_by_cid.sql");
		cp_f1_get_customer_by_taxid_sql = Dml.load("customer_position_frm1_get_customer_by_taxid.sql");

		if (DbPool.getDbType() == DbType.MSSQL)
		{
			cp_f1_get_assets_sql = Dml.load("customer_position_frm1_get_assets_ms.sql");
			cp_f1_get_trade_history_sql = Dml.load("customer_position_frm1_get_trade_history_ms.sql");
		}
		else if (DbPool.getDbType() == DbType.MYSQL)
		{
			cp_f1_get_assets_sql = Dml.load("customer_position_frm1_get_assets.sql");
			cp_f1_get_trade_history_sql = Dml.load("customer_position_frm1_get_trade_history_my.sql");
		}		
		else
		{
			cp_f1_get_assets_sql = Dml.load("customer_position_frm1_get_assets.sql");
			cp_f1_get_trade_history_sql = Dml.load("customer_position_frm1_get_trade_history.sql");
		}
	}

	private static final ResultSetHandler<Customer> rs_f1_get_customer = new ResultSetHandler<Customer>()
	{
		@Override
		public Customer process(ResultSet rs)
		{
			try
			{
				Customer obj = new Customer();

				obj.c_id = rs.getLong("c_id");
				obj.c_tax_id = rs.getString("c_tax_id");
				obj.c_st_id = rs.getString("c_st_id");
				obj.c_l_name = rs.getString("c_l_name");
				obj.c_f_name = rs.getString("c_f_name");
				obj.c_m_name = rs.getString("c_m_name");
				obj.c_gndr = rs.getString("c_gndr");
				obj.c_tier = rs.getInt("c_tier");
				obj.c_dob = rs.getDate("c_dob");
				obj.c_ad_id = rs.getLong("c_ad_id");
				obj.c_ctry_1 = rs.getString("c_ctry_1");
				obj.c_area_1 = rs.getString("c_area_1");
				obj.c_local_1 = rs.getString("c_local_1");
				obj.c_ext_1 = rs.getString("c_ext_1");
				obj.c_ctry_2 = rs.getString("c_ctry_2");
				obj.c_area_2 = rs.getString("c_area_2");
				obj.c_local_2 = rs.getString("c_local_2");
				obj.c_ext_2 = rs.getString("c_ext_2");
				obj.c_ctry_3 = rs.getString("c_ctry_3");
				obj.c_area_3 = rs.getString("c_area_3");
				obj.c_local_3 = rs.getString("c_local_3");
				obj.c_ext_3 = rs.getString("c_ext_3");
				obj.c_email_1 = rs.getString("c_email_1");
				obj.c_email_2 = rs.getString("c_email_2");

				return obj;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<CustomerAsset> rs_f1_get_assets = new ResultSetHandler<CustomerAsset>()
	{
		@Override
		public CustomerAsset process(ResultSet rs)
		{
			try
			{
				CustomerAsset obj = new CustomerAsset(rs.getLong("acct_id"), rs.getBigDecimal("cash_bal"), rs.getBigDecimal("assets_total"));
				return obj;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetHandler<TradeHistory> rs_f1_get_trade_history = new ResultSetHandler<TradeHistory>()
	{
		@Override
		public TradeHistory process(ResultSet rs)
		{
			try
			{
				TradeHistory obj = new TradeHistory(rs.getLong("t_id"), rs.getString("t_s_symb"), rs.getInt("t_qty"), rs.getString("st_name"), rs.getTimestamp("th_dts"));
				return obj;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private final CEInputGeneration gen;
	private final String tx_name;

	public TxCustomerPosition(CEInputGeneration gen)
	{
		super();
		this.gen = gen;
		tx_name = "Customer-Position";
	}

	@Override
	public String name()
	{
		return tx_name;
	}

	@Override
	protected final TxOutput run()
	{
		final TCustomerPositionFrame1Input frm1_input = gen.generateCustomerPositionInput();

		TCustomerPositionFrame1Output frm1_output = executeFrame1(frm1_input);

		if (log.isDebugEnabled())
		{
			log.debug(frm1_output.toString());
		}

		TCustomerPositionFrame2Output frm2_output;
		if (frm1_output.status > -1)
		{
			if (frm1_input.get_history)
			{
				TCustomerPositionFrame2Input frm2_input = new TCustomerPositionFrame2Input();

				if (frm1_output.lst_asset != null)
				{
					CustomerAsset ca = frm1_output.lst_asset.get(frm1_input.acct_id_idx);
					if (ca != null)
					{
						frm2_input.acct_id = ca.acct_id;
					}
				}

				frm2_output = executeFrame2(frm2_input);
			}
			else
			{
				return frm1_output;
			}
		}
		else
		{
			List<TradeHistory> lst_trade_history = Collections.emptyList();
			frm2_output = new TCustomerPositionFrame2Output(frm1_output.status, lst_trade_history);
		}

		return frm2_output;
	}

	private TCustomerPositionFrame1Output executeFrame1(final TCustomerPositionFrame1Input frm1_input)
	{
		if (log.isDebugEnabled())
		{
			log.debug(frm1_input.toString());
		}

		Customer customer = getCustomer(frm1_input.cust_id, frm1_input.tax_id);

		List<CustomerAsset> lst_asset;

		if (customer != null)
		{
			DbFetcher<CustomerAsset> db = new DbFetcher<CustomerAsset>();

			lst_asset = db.fetchList(cp_f1_get_assets_sql, rs_f1_get_assets, customer.c_id);
		}
		else
		{
			lst_asset = Collections.emptyList();
		}

		int status = 0;

		if ((lst_asset.size() < 1) || (lst_asset.size() > max_acct_len))
		{
			status = -221;
		}

		TCustomerPositionFrame1Output frm1_output = new TCustomerPositionFrame1Output(status, customer, lst_asset);
		return frm1_output;
	}

	private TCustomerPositionFrame2Output executeFrame2(TCustomerPositionFrame2Input frm2_input)
	{
		if (log.isDebugEnabled())
		{
			log.debug(frm2_input.toString());
		}

		List<TradeHistory> lst_trade_history;

		if (frm2_input != null)
		{
			DbFetcher<TradeHistory> db = new DbFetcher<TradeHistory>();

			lst_trade_history = db.fetchList(cp_f1_get_trade_history_sql, rs_f1_get_trade_history, frm2_input.acct_id);
		}
		else
		{
			lst_trade_history = Collections.emptyList();
		}

		int status = 0;
		if ((lst_trade_history.size() < 1) || (lst_trade_history.size() > max_hist_len))
		{
			status = -221;
		}

		TCustomerPositionFrame2Output frm2_output = new TCustomerPositionFrame2Output(status, lst_trade_history);

		if (log.isDebugEnabled())
		{
			log.debug(frm2_output.toString());
		}

		return frm2_output;
	}

	private Customer getCustomer(long cid, String tax_id)
	{
		DbFetcher<Customer> db = new DbFetcher<Customer>();

		Customer obj = null;

		if (cid > 0)
		{
			obj = db.fetchObject(cp_f1_get_customer_by_cid_sql, rs_f1_get_customer, cid);
		}
		else if (StringUtils.isNotBlank(tax_id))
		{
			obj = db.fetchObject(cp_f1_get_customer_by_taxid_sql, rs_f1_get_customer, tax_id);
		}

		return obj;
	}
}