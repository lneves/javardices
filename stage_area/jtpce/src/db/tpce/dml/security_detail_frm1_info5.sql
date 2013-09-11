SELECT
	lt_price
	, lt_open_price
	, lt_vol
FROM
	last_trade
WHERE
	lt_s_symb = ?;