SELECT
	t_id, t_ca_id, t_exec_name, t_is_cash, t_trade_price, t_qty, t_dts, t_tt_id
	, se_amt
	, se_cash_due_date
	, se_cash_type
	, ct_amt
	, ct_name
FROM
	(
	SELECT TOP 20
		t_id, t_ca_id, t_exec_name, t_is_cash, t_trade_price, t_qty, t_dts, t_tt_id
	FROM
		trade
	WHERE
		t_s_symb = ?
		AND t_dts BETWEEN ? AND ?
	ORDER BY t_dts
	) AS trades
	LEFT JOIN cash_transaction ON t_id = ct_t_id
	INNER JOIN settlement ON se_t_id = trades.t_id;