SELECT
	SUM(price) / COUNT(*) AS TSIA
FROM
	quote
WHERE
	symbol LIKE 's:1__';