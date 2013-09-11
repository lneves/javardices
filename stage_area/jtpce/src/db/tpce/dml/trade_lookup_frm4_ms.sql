SELECT
	hh_h_t_id
	, hh_t_id
	, hh_before_qty
	, hh_after_qty
FROM
	holding_history
WHERE
	hh_h_t_id IN (SELECT hh_h_t_id FROM holding_history  WHERE hh_t_id IN (SELECT TOP 1 t_id FROM trade WHERE t_ca_id = ? AND t_dts >= ? ORDER BY t_dts ASC));