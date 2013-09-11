SELECT
	t_id, t_bid_price, t_exec_name, t_is_cash, t_trade_price, tt_is_mrkt
	, se_amt
	, se_cash_due_date
	, se_cash_type
	, ct_amt
	, ct_name
FROM
	trade
	INNER JOIN trade_type ON t_tt_id = tt_id
	INNER JOIN settlement ON se_t_id = t_id
	LEFT JOIN cash_transaction ON t_id = ct_t_id	
WHERE
	t_id IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
	AND t_tt_id = tt_id;