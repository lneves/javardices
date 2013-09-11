SELECT
	t_id, t_exec_name, t_bid_price, t_trade_price, t_is_cash
	, se_amt
	, se_cash_due_date
	, se_cash_type
	, ct_amt
	, ct_name
FROM
	(
	SELECT TOP 20
		t_id, t_exec_name, t_bid_price, t_trade_price, t_is_cash
	FROM
		trade
	WHERE
		t_ca_id = ?
		AND t_dts BETWEEN ? AND ?
	ORDER BY t_dts

	) AS trades
	LEFT JOIN cash_transaction ON t_id = ct_t_id
	INNER JOIN settlement ON se_t_id = trades.t_id;