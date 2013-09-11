SELECT
	b_name
	, SUM(tr_qty * tr_bid_price) AS volume
FROM
	trade_request, sector, industry, company, broker, security
WHERE
	tr_b_id = b_id
	AND tr_s_symb = s_symb
	AND s_co_id = co_id
	AND co_in_id = in_id
	AND sc_id = in_sc_id
	AND b_name IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
	AND sc_name = ?
GROUP BY b_name
ORDER BY 2 DESC;