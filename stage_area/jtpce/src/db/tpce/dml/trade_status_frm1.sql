SELECT
	 c_l_name AS cust_l_name
	, c_f_name AS cust_f_name
	, b_name AS broker_name
	, t_chrg AS charge
	, t_exec_name AS exec_name
	, ex_name
	, s_name
	, st_name AS status_name
	, t_s_symb AS symbol
	, t_dts AS trade_dts
	, t_id AS trade_id
	, t_qty AS trade_qty
	, tt_name AS type_name
FROM
	(SELECT * FROM trade WHERE t_ca_id = ? ORDER BY t_dts DESC LIMIT 50) AS tt
	, status_type
	, trade_type
	, security 
	, exchange 
	, (SELECT c_l_name, c_f_name, b_name FROM customer_account, customer, broker WHERE ca_id = ? AND c_id = ca_c_id AND b_id = ca_b_id) as c_info
WHERE
	st_id = t_st_id
	AND tt_id = t_tt_id
	AND s_symb = t_s_symb
	AND ex_id = s_ex_id
ORDER BY t_dts DESC;