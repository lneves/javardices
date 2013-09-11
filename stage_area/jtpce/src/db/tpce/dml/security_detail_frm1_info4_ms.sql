SELECT TOP %s
	dm_date
	, dm_close
	, dm_high
	, dm_low
	, dm_vol
FROM
	daily_market
WHERE
	dm_s_symb = ?
	AND dm_date >= ?
ORDER BY dm_date ASC;