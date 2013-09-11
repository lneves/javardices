UPDATE
	orders
SET
	orderstatus = ?
	, completiondate = ?
WHERE
	orderid= ?;