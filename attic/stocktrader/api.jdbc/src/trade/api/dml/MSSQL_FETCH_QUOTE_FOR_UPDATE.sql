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
WITH (REPEATABLEREAD, UPDLOCK)
WHERE
	symbol = ?;