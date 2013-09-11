SELECT
	ca_id AS acct_id
	, ca_bal AS cash_bal
	, SUM(COALESCE(hs_qty * lt_price, 0)) AS assets_total
FROM
	customer_account
	LEFT JOIN holding_summary ON ca_id=hs_ca_id
	LEFT JOIN last_trade ON hs_s_symb = lt_s_symb
WHERE
	ca_c_id = ?
	AND lt_s_symb = hs_s_symb
GROUP BY ca_id, ca_bal
ORDER BY 3 ASC LIMIT 10;