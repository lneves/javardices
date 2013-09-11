SELECT
	symbol
	, companyname
	, price
	, open1
	, volume
	, low
	, high
	, change1
FROM
	quote
WHERE
	symbol = ?
FOR UPDATE;