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
	account_accountid IN (SELECT accountid FROM account WHERE profile_userid=?)
	AND orderstatus = 'closed'	
ORDER BY
	orderid DESC;