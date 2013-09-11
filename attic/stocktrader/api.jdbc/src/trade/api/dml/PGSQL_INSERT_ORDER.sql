INSERT INTO orders
	(account_accountid, quote_symbol, holding_holdingid, ordertype, orderstatus, orderfee, price, quantity, opendate)
VALUES
	(?, ?, ?,  ?, ?, ?, ?, ?, ?)
RETURNING orderid;