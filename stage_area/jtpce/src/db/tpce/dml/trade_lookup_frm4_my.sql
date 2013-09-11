SELECT
	holding_history.hh_h_t_id
	, holding_history.hh_t_id
	, holding_history.hh_before_qty
	, holding_history.hh_after_qty
FROM
	holding_history
	, (
	SELECT
			hh_h_t_id
		FROM
			holding_history
			, (SELECT t_id FROM trade WHERE t_ca_id = ? AND t_dts >= ? ORDER BY t_dts ASC LIMIT 1) AS h0
		WHERE holding_history.hh_t_id = h0.t_id
	) AS h1 
WHERE
	holding_history.hh_h_t_id = h1.hh_h_t_id;