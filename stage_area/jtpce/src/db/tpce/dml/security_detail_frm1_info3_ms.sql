SELECT TOP 20
	fi_year
	, fi_qtr
	, fi_qtr_start_date
	, fi_revenue
	, fi_net_earn
	, fi_basic_eps
	, fi_dilut_eps
	, fi_margin
	, fi_inventory
	, fi_assets
	, fi_liability
	, fi_out_basic
	, fi_out_dilut
FROM
	financial
WHERE
	fi_co_id = ?
ORDER BY
	fi_year ASC, fi_qtr;