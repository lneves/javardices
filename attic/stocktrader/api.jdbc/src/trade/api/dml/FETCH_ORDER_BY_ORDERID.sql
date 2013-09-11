SELECT 
	orderid
	, account_accountid
	, quote_symbol
	, holding_holdingid
	, ordertype
	, orderstatus
	, orderfee
	, price
	, quantity
	, opendate
	, completiondate
FROM 
	orders
WHERE
	orderid = ?;