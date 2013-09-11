package db.tpce.common;

import java.math.BigDecimal;
import java.sql.Date;

public class SecurityDetail_Info1
{
	public final String s_name;
	public final long co_id;
	public final String co_name;
	public final String co_sp_rate;
	public final String co_ceo;
	public final String co_desc;
	public final java.sql.Date co_open_date;
	public final String co_st_id;
	public final String co_ad_line1;
	public final String co_ad_line2;
	public final String co_zc_town;
	public final String co_zc_div;
	public final String co_ad_zc_code;
	public final String co_ad_ctry;
	public final long s_num_out;
	public final java.sql.Date s_start_date;
	public final java.sql.Date s_exch_date;
	public final java.math.BigDecimal s_pe;
	public final java.math.BigDecimal s_52wk_high;
	public final java.sql.Date s_52wk_high_date;
	public final java.math.BigDecimal s_52wk_low;
	public final java.sql.Date s_52wk_low_date;
	public final java.math.BigDecimal s_dividend;
	public final java.math.BigDecimal s_yield;
	public final String ex_ad_line1;
	public final String ex_ad_line2;
	public final String ex_zc_town;
	public final String ex_zc_div;
	public final String ex_ad_ctry;
	public final String ex_ad_zc_code;
	public final String ex_desc;
	public final String ex_name;
	public final int ex_num_symb;
	public final int ex_open;
	public final int ex_close;

	public SecurityDetail_Info1(String s_name, long co_id, String co_name, String co_sp_rate, String co_ceo, String co_desc, Date co_open_date, String co_st_id, String co_ad_line1, String co_ad_line2, String co_zc_town, String co_zc_div, String co_ad_zc_code, String co_ad_ctry, long s_num_out, Date s_start_date, Date s_exch_date, BigDecimal s_pe, BigDecimal s_52wk_high, Date s_52wk_high_date, BigDecimal s_52wk_low, Date s_52wk_low_date, BigDecimal s_dividend, BigDecimal s_yield, String ex_ad_line1, String ex_ad_line2, String ex_zc_town, String ex_zc_div, String ex_ad_ctry, String ex_ad_zc_code, String ex_desc, String ex_name, int ex_num_symb, int ex_open, int ex_close)
	{
		super();
		this.s_name = s_name;
		this.co_id = co_id;
		this.co_name = co_name;
		this.co_sp_rate = co_sp_rate;
		this.co_ceo = co_ceo;
		this.co_desc = co_desc;
		this.co_open_date = co_open_date;
		this.co_st_id = co_st_id;
		this.co_ad_line1 = co_ad_line1;
		this.co_ad_line2 = co_ad_line2;
		this.co_zc_town = co_zc_town;
		this.co_zc_div = co_zc_div;
		this.co_ad_zc_code = co_ad_zc_code;
		this.co_ad_ctry = co_ad_ctry;
		this.s_num_out = s_num_out;
		this.s_start_date = s_start_date;
		this.s_exch_date = s_exch_date;
		this.s_pe = s_pe;
		this.s_52wk_high = s_52wk_high;
		this.s_52wk_high_date = s_52wk_high_date;
		this.s_52wk_low = s_52wk_low;
		this.s_52wk_low_date = s_52wk_low_date;
		this.s_dividend = s_dividend;
		this.s_yield = s_yield;
		this.ex_ad_line1 = ex_ad_line1;
		this.ex_ad_line2 = ex_ad_line2;
		this.ex_zc_town = ex_zc_town;
		this.ex_zc_div = ex_zc_div;
		this.ex_ad_ctry = ex_ad_ctry;
		this.ex_ad_zc_code = ex_ad_zc_code;
		this.ex_desc = ex_desc;
		this.ex_name = ex_name;
		this.ex_num_symb = ex_num_symb;
		this.ex_open = ex_open;
		this.ex_close = ex_close;
	}

	@Override
	public String toString()
	{
		return String.format("SecurityDetail_Info1 {s_name=%s, co_id=%s, co_name=%s, co_sp_rate=%s, co_ceo=%s, co_desc=%s, co_open_date=%s, co_st_id=%s, co_ad_line1=%s, co_ad_line2=%s, co_zc_town=%s, co_zc_div=%s, co_ad_zc_code=%s, co_ad_ctry=%s, s_num_out=%s, s_start_date=%s, s_exch_date=%s, s_pe=%s, s_52wk_high=%s, s_52wk_high_date=%s, s_52wk_low=%s, s_52wk_low_date=%s, s_dividend=%s, s_yield=%s, ex_ad_line1=%s, ex_ad_line2=%s, ex_zc_town=%s, ex_zc_div=%s, ex_ad_ctry=%s, ex_ad_zc_code=%s, ex_desc=%s, ex_name=%s, ex_num_symb=%s, ex_open=%s, ex_close=%s}", s_name, co_id, co_name, co_sp_rate, co_ceo, co_desc, co_open_date, co_st_id, co_ad_line1, co_ad_line2, co_zc_town, co_zc_div, co_ad_zc_code, co_ad_ctry, s_num_out, s_start_date, s_exch_date, s_pe, s_52wk_high, s_52wk_high_date,
				s_52wk_low, s_52wk_low_date, s_dividend, s_yield, ex_ad_line1, ex_ad_line2, ex_zc_town, ex_zc_div, ex_ad_ctry, ex_ad_zc_code, ex_desc, ex_name, ex_num_symb, ex_open, ex_close);
	}
}