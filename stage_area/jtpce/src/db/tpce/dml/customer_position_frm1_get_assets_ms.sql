SELECT TOP 10
	ca_id AS acct_id
	, ca_bal AS cash_bal
	, SUM(COALESCE(hs_qty * lt_price, 0)) AS assets_total
FROM
	customer_account
	LEFT LOOP JOIN holding_summary ON ca_id=hs_ca_id
	LEFT LOOP JOIN last_trade ON hs_s_symb = lt_s_symb
WHERE
	ca_c_id = ?
GROUP BY ca_id, ca_bal
ORDER BY 3 ASC
OPTION (FORCE ORDER);