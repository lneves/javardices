UPDATE
	quote
SET
	price = ?
	, change1 = ( ? - open1 )
	, volume = ?
WHERE
	symbol = ?