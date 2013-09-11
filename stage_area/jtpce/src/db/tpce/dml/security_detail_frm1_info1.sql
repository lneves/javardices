SELECT
	s_name
	, co_id
	, co_name
	, co_sp_rate
	, co_ceo
	, co_desc
	, co_open_date
	, co_st_id
	, ca.ad_line1 AS co_ad_line1
	, ca.ad_line2 AS co_ad_line2
	, zca.zc_town AS co_zc_town
	, zca.zc_div AS co_zc_div
	, ca.ad_zc_code AS co_ad_zc_code
	, ca.ad_ctry AS co_ad_ctry
	, s_num_out
	, s_start_date
	, s_exch_date
	, s_pe
	, s_52wk_high
	, s_52wk_high_date
	, s_52wk_low
	, s_52wk_low_date
	, s_dividend
	, s_yield
	, ea.ad_line1 AS ex_ad_line1
	, ea.ad_line2 AS ex_ad_line2
	, zea.zc_town AS ex_zc_town
	, zea.zc_div AS ex_zc_div
	, ea.ad_ctry AS ex_ad_ctry
	, ea.ad_zc_code AS ex_ad_zc_code
	, ex_desc
	, ex_name
	, ex_num_symb
	, ex_open
	, ex_close
FROM
	security
	, company
	, address ca
	, address ea
	, zip_code zca
	, zip_code zea
	, exchange
WHERE
	s_symb = ?
	AND co_id = s_co_id
	AND ca.ad_id = co_ad_id
	AND ea.ad_id = ex_ad_id
	AND ex_id = s_ex_id
	AND ca.ad_zc_code = zca.zc_code
	AND ea.ad_zc_code = zea.zc_code

