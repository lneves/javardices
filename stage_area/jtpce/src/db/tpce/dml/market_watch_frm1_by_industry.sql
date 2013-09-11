SELECT 100 * ((SUM(new_mkt_cap) / SUM(old_mkt_cap)) -1)  AS pct_change
FROM
(
SELECT
	(s_num_out * old_price) AS old_mkt_cap,
	(s_num_out * new_price) AS new_mkt_cap
FROM
	(
	SELECT
		lt_price AS new_price
		, dm_close AS old_price
		, s_num_out
	FROM
		last_trade
		, daily_market
		, security
		, (SELECT s_symb AS x_s_symb FROM industry, company, security WHERE in_name = ? AND co_in_id = in_id AND co_id BETWEEN ? AND ? AND s_co_id = co_id) AS x
	WHERE
		lt_s_symb = x.x_s_symb
		AND dm_s_symb = x.x_s_symb
		AND s_symb = x.x_s_symb
		AND dm_date = ?
		AND dm_s_symb = lt_s_symb
		AND dm_s_symb = s_symb
	) AS s
)
AS market_watch_fr1
WHERE old_mkt_cap <> 0;