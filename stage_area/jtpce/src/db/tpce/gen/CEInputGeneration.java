package db.tpce.gen;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import db.bench.DbRandom;
import db.tpce.common.CustomerId;
import db.tpce.input.TBrokerVolumeInput;
import db.tpce.input.TCustomerPositionFrame1Input;
import db.tpce.input.TMarketWatchInput;
import db.tpce.input.TSecurityDetailInput;
import db.tpce.input.TTradeLookupInput;
import db.tpce.input.TTradeStatusInput;
import db.tpce.tx.TxBrokerVolume;
import db.tpce.tx.TxTradeLookup;

public class CEInputGeneration
{

	private final DbRandom drand = new DbRandom();

	public TBrokerVolumeInput generateBrokerVolumeInput()
	{
		TBrokerVolumeInput param = new TBrokerVolumeInput();

		Set<String> broker_set = new HashSet<String>();
		// int broker_len = drand.rndIntRange(TxBrokerVolume.min_broker_list_len, TxBrokerVolume.max_broker_list_len);
		int broker_len = drand.rndIntRange(5, 10);

		while (broker_set.size() < broker_len)
		{
			String n = BrokerSelection.getRandom().b_name;
			broker_set.add(n);
		}

		String[] a_brk = new String[TxBrokerVolume.max_broker_list_len];

		int ix = 0;
		for (String brk : broker_set)
		{
			a_brk[ix++] = brk;
		}

		param.broker_list = a_brk;
		param.sector_name = SectorSelection.get().sc_name;

		return param;
	}

	public TCustomerPositionFrame1Input generateCustomerPositionInput()
	{
		TCustomerPositionFrame1Input param = new TCustomerPositionFrame1Input();

		CustomerId c = CustomerIdSelection.getNonUniformRandom();

		if (drand.rndPercent(GenConstants.cp_by_tax_id_percent))
		{
			// send tax id instead of customer id
			param.tax_id = c.c_tax_id;

			param.cust_id = 0; // don't need customer id since filled in the tax
			// id
		}
		else
		{
			// send customer id and not the tax id
			param.cust_id = c.c_id;

			param.tax_id = "";
		}

		param.get_history = drand.rndPercent(GenConstants.cp_get_history_percent);

		if (param.get_history)
		{
			param.acct_id_idx = drand.rndIntRange(0, c.count_ca - 1);
		}
		else
		{
			param.acct_id_idx = -1;
		}

		return param;
	}

	public TMarketWatchInput generateMarketWatchInput()
	{
		long c_id;
		long acct_id;
		long starting_co_id;
		long ending_co_id;
		String industry_name;
		java.sql.Date start_date;

		int iThreshold = drand.rndIntRange(1, 100);

		if (iThreshold <= GenConstants.mw_by_industry)
		{

			industry_name = IndustrySelection.getRandom();
			c_id = 0;
			acct_id = 0;

			int iActiveCompanyCount = CompanySelection.getCount();
			long iStartFromCompany = CompanySelection.get(0);

			if (GenConstants.iBaseCompanyCount < iActiveCompanyCount)
			{
				starting_co_id = drand.rndLongRange(iStartFromCompany, iStartFromCompany + iActiveCompanyCount - (GenConstants.iBaseCompanyCount - 1));
				ending_co_id = starting_co_id + (GenConstants.iBaseCompanyCount - 1);
			}
			else
			{
				starting_co_id = iStartFromCompany;
				ending_co_id = iStartFromCompany + iActiveCompanyCount - 1;
			}
		}
		else
		{
			industry_name = null;
			starting_co_id = 0;
			ending_co_id = 0;

			CustomerId c = CustomerIdSelection.getNonUniformRandom();

			if (iThreshold <= (GenConstants.mw_by_industry + GenConstants.mw_by_watch_list))
			{
				// Send customer id
				c_id = c.c_id;
				acct_id = 0;
			}
			else
			{
				// Send account id
				long start_acct_id = c.min_ca_id;
				int count_acct = c.count_ca;

				acct_id = start_acct_id + drand.rndIntRange(0, count_acct - 1);

				c_id = 0;
			}
		}

		start_date = DailyMarketSelection.getNonUniformRandom();

		TMarketWatchInput param = new TMarketWatchInput(c_id, acct_id, starting_co_id, ending_co_id, industry_name, start_date);

		return param;
	}

	public TTradeStatusInput generateTradeStatusInput()
	{
		CustomerId c = CustomerIdSelection.getNonUniformRandom();

		long acct_id;
		long start_acct_id = c.min_ca_id;
		int count_acct = c.count_ca;

		acct_id = start_acct_id + drand.rndIntRange(0, count_acct - 1);

		TTradeStatusInput param = new TTradeStatusInput(acct_id);
		return param;
	}

	public TSecurityDetailInput generateSecurityDetailInput()
	{
		final boolean access_lob_flag = drand.rndPercent(50);
		final int max_rows_to_return = drand.rndIntRange(5, 20);
		int sday = DailyMarketSelection.getRandomIndex() - max_rows_to_return;
		final java.sql.Date start_day = new java.sql.Date(DailyMarketSelection.getByIndex(sday).getTime());
		String symbol = SymbolSelection.getRandom();

		TSecurityDetailInput param = new TSecurityDetailInput(access_lob_flag, max_rows_to_return, start_day, symbol);

		return param;
	}

	public TTradeLookupInput generateTradeLookupInput()
	{
		int frame_to_execute; // which of the frames to execute
		long[] trade_id;
		long acct_id;
		long max_acct_id;
		int max_trades;

		Date start_trade_dts;
		Date end_trade_dts;
		String symbol;

		int f1 = GenConstants.tradeLookup_do_frame1;
		int f2 = GenConstants.tradeLookup_do_frame1 + GenConstants.tradeLookup_do_frame2;
		int f3 = GenConstants.tradeLookup_do_frame1 + GenConstants.tradeLookup_do_frame2 + GenConstants.tradeLookup_do_frame3;

		long min_t_tds = 1104742802L;
		long max_t_tds = min_t_tds + (24L * 3600L);

		int iThreshold = drand.rndIntRange(1, 100);

		if (iThreshold <= f1)
		{
			frame_to_execute = 1;

			acct_id = 0;
			max_acct_id = 0;
			start_trade_dts = null;
			end_trade_dts = null;
			max_trades = TxTradeLookup.TradeLookupFrame1MaxRows;
			symbol = "";

			Set<Long> trade_id_set = new HashSet<Long>();

			while (trade_id_set.size() < max_trades)
			{
				long t = drand.nonUniformRandom(1, 29088000, GenConstants.TradeLookupAValueForTradeIDGenFrame1, GenConstants.TradeLookupSValueForTradeIDGenFrame1);

				if (GenConstants.iAbortedTradeModFactor == t % GenConstants.iAbortTrade)
				{
					t++;
				}

				trade_id_set.add(t + GenConstants.iTTradeShift);
			}

			trade_id = new long[trade_id_set.size()];
			int i = 0;
			for (Long l : trade_id_set)
			{
				trade_id[i++] = l;
			}
		}
		else if ((iThreshold > f1) && (iThreshold <= f2))
		{
			frame_to_execute = 2;

			CustomerId c = CustomerIdSelection.getNonUniformRandom();
			acct_id = c.min_ca_id + drand.rndIntRange(0, c.count_ca - 1);

			// TODO: revisit the generation of this parameter, i don't
			// understand the EGen code that generates this value
			long start_trade_dts_time = 0L;

			do
			{
				start_trade_dts_time = drand.nonUniformRandom(min_t_tds, max_t_tds) * 1000L;
			}
			while (is_not_valid_trade_date(start_trade_dts_time));

			start_trade_dts = new Date(start_trade_dts_time);

			// 15 minute interval
			end_trade_dts = new Date(start_trade_dts_time + (15L * 60L * 1000L));

			trade_id = new long[0];
			max_acct_id = 0;
			max_trades = TxTradeLookup.TradeLookupFrame2MaxRows;
			symbol = "";

		}
		else if ((iThreshold > f2) && (iThreshold <= f3))
		{
			frame_to_execute = 3;

			trade_id = new long[0];
			max_trades = TxTradeLookup.TradeLookupFrame3MaxRows;
			acct_id = 0;
			symbol = SymbolSelection.geNonUniformtRandom(GenConstants.TradeLookupAValueForSymbolFrame3, GenConstants.TradeLookupSValueForSymbolFrame3);

			// FROM TPCE Spec -> The max_acct_id "where" clause is a hook used
			// for engineering purposes only and is not required for benchmark
			// publication purposes.
			max_acct_id = 0;

			long start_trade_dts_time = 0L;

			do
			{
				start_trade_dts_time = drand.rndLongRange(min_t_tds, max_t_tds) * 1000L;
			}
			while (is_not_valid_trade_date(start_trade_dts_time));

			start_trade_dts = new Date(start_trade_dts_time);

			// 15 minute interval
			end_trade_dts = new Date(start_trade_dts_time + (15L * 60L * 1000L));
		}
		else
		{
			frame_to_execute = 4;

			CustomerId c = CustomerIdSelection.getNonUniformRandom();
			acct_id = c.min_ca_id + drand.rndIntRange(0, c.count_ca - 1);

			long start_trade_dts_time = drand.rndLongRange(min_t_tds, max_t_tds);
			start_trade_dts = new Date(start_trade_dts_time * 1000);

			trade_id = new long[0];
			max_acct_id = 0;
			max_trades = TxTradeLookup.TradeLookupFrame4MaxRows;
			symbol = "";
			end_trade_dts = null;

		}

		TTradeLookupInput param = new TTradeLookupInput(frame_to_execute, trade_id, acct_id, max_acct_id, max_trades, start_trade_dts, end_trade_dts, symbol);

		return param;
	}

	public TTradeLookupInput generateSimpleTradeLookupInput()
	{
		int frame_to_execute; // which of the frames to execute
		long[] trade_id;
		long acct_id;
		long max_acct_id;
		int max_trades;

		Date start_trade_dts;
		Date end_trade_dts;
		String symbol;

		long min_t_tds = 1104742802L;
		long max_t_tds = min_t_tds + (24L * 3600L);

		int iThreshold = drand.rndIntRange(1, 100);

		if (iThreshold <= 50)
		{
			frame_to_execute = 1;

			acct_id = 0;
			max_acct_id = 0;
			start_trade_dts = null;
			end_trade_dts = null;
			max_trades = 1;
			symbol = "";

			Set<Long> trade_id_set = new HashSet<Long>();

			while (trade_id_set.size() < max_trades)
			{
				long t = drand.nonUniformRandom(1, 29088000, GenConstants.TradeLookupAValueForTradeIDGenFrame1, GenConstants.TradeLookupSValueForTradeIDGenFrame1);

				if (GenConstants.iAbortedTradeModFactor == t % GenConstants.iAbortTrade)
				{
					t++;
				}

				trade_id_set.add(t + GenConstants.iTTradeShift);
			}

			trade_id = new long[trade_id_set.size()];
			int i = 0;
			for (long l : trade_id_set)
			{
				trade_id[i++] = l;
			}
		}
		else
		{
			frame_to_execute = 4;

			CustomerId c = CustomerIdSelection.getNonUniformRandom();
			acct_id = c.min_ca_id + drand.rndIntRange(0, c.count_ca - 1);

			long start_trade_dts_time = drand.rndLongRange(min_t_tds, max_t_tds);
			start_trade_dts = new Date(start_trade_dts_time * 1000);

			trade_id = new long[0];
			max_acct_id = 0;
			max_trades = TxTradeLookup.TradeLookupFrame4MaxRows;
			symbol = "";
			end_trade_dts = null;

		}

		TTradeLookupInput param = new TTradeLookupInput(frame_to_execute, trade_id, acct_id, max_acct_id, max_trades, start_trade_dts, end_trade_dts, symbol);

		return param;
	}

	private boolean is_not_valid_trade_date(long startTradeDtsTime)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(startTradeDtsTime);

		int dow = cal.get(Calendar.DAY_OF_WEEK);
		int hod = cal.get(Calendar.HOUR_OF_DAY);

		if ((dow == Calendar.SATURDAY) || (dow == Calendar.SUNDAY))
		{
			return true;
		}
		if ((hod < 9) || (hod > 17))
		{
			return true;
		}

		return false;
	}
}