SELECT
	th_dts
	, th_st_id
FROM
	trade_history
WHERE
	th_t_id = ?
ORDER BY th_dts;