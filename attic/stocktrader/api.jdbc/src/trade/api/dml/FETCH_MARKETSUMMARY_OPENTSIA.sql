SELECT
	SUM(open1) / COUNT(*) AS openTSIA
FROM
	quote
WHERE
	symbol LIKE 's:1__';