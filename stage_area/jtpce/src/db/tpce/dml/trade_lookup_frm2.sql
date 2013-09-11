SELECT
	t_id, t_bid_price, t_exec_name, t_is_cash, t_trade_price
	, se_amt
	, se_cash_due_date
	, se_cash_type
	, ct_amt
	, ct_name
FROM
	trade
	INNER JOIN settlement ON se_t_id = t_id
	LEFT JOIN cash_transaction ON t_id = ct_t_id	
WHERE
	t_ca_id = ?
	AND t_dts BETWEEN ? AND ?
ORDER BY t_dts LIMIT 20