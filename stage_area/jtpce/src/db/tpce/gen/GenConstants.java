package db.tpce.gen;

public class GenConstants
{
	public static final int cp_by_cust_id_percent = 50;
	public static final int cp_by_tax_id_percent = 50;
	public static final int cp_get_history_percent = 50;

	public static final int mw_by_acct_id = 35;
	public static final int mw_by_industry = 5;
	public static final int mw_by_watch_list = 60;

	public static final int iBaseCompanyCount = 5000; // number of base companies in the flat file
	public static final int iBaseCompanyCompetitorCount = 3 * iBaseCompanyCount; // number of base company competitor rows

	public static final long daily_market_base_ts = 946857600L * 1000L; // Mon Jan 03 00:00:00 WET 2000

	public static final int TradeLookupAValueForTradeIDGenFrame1 = 65535;
	public static final int TradeLookupSValueForTradeIDGenFrame1 = 7;
	public static final int TradeLookupAValueForTimeGenFrame2 = 4095;
	public static final int TradeLookupSValueForTimeGenFrame2 = 16;
	public static final int TradeLookupAValueForSymbolFrame3 = 0;
	public static final int TradeLookupSValueForSymbolFrame3 = 0;
	public static final int TradeLookupAValueForTimeGenFrame3 = 4095;
	public static final int TradeLookupSValueForTimeGenFrame3 = 16;
	public static final int TradeLookupAValueForTimeGenFrame4 = 4095;
	public static final int TradeLookupSValueForTimeGenFrame4 = 16;

	public static final int tradeLookup_do_frame1 = 30;
	public static final int tradeLookup_do_frame2 = 30;
	public static final int tradeLookup_do_frame3 = 30;
	public static final int tradeLookup_do_frame4 = 10;

	public static final long iTTradeShift = 200000000000000L;
	// At what trade count multiple to abort trades.
	// One trade in every iAboutTrade block is aborted (trade id is thrown out).
	// NOTE: this really is 10 * Trade-Order mix percentage!
	//
	public static final int iAbortTrade = 101;

	// Used at load and run time to determine which intial trades
	// simulate rollback by "aborting" - I.e. used to skip over a
	// trade ID.
	public static final int iAbortedTradeModFactor = 51;

	public static final long StartTradeTS = 1104742802L;
	public static final long EndTradeTS = StartTradeTS + (24L * 3600L * 1000L);

}
