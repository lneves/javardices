SELECT TOP 30
	t_id
	, t_s_symb
	, t_qty
	, st_name
	, th_dts
FROM
	(
	SELECT TOP 10
		t_id AS id
	FROM
		trade
	WHERE
		t_ca_id = ?
	ORDER BY t_dts DESC
	) AS t
	JOIN trade ON t_id=id
	INNER LOOP JOIN trade_history ON th_t_id=t_id
	, status_type
WHERE
	st_id = th_st_id
ORDER BY th_dts DESC
OPTION  (FORCE ORDER);