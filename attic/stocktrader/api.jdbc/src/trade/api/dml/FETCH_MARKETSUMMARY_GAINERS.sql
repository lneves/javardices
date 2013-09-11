SELECT
	symbol
	, companyname
	, volume
	, price
	, open1
	, low
	, high
	, change1
FROM
	quote
WHERE
	symbol LIKE 's:1__'
ORDER BY
	change1 ASC;