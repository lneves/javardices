SELECT
	t_id
	, t_s_symb
	, t_qty
	, st_name
	, th_dts
FROM
	(
	SELECT t_id AS id
	FROM trade
	WHERE t_ca_id = ?
	ORDER BY t_dts DESC LIMIT 10
	) AS t
	, trade
	, trade_history
	, status_type FORCE INDEX(PRIMARY)
WHERE
	t_id = id
	AND th_t_id = t_id
	AND st_id = th_st_id
ORDER BY th_dts DESC LIMIT 30