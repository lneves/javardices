SET NOCOUNT ON;
INSERT INTO orders
	(account_accountid, quote_symbol, holding_holdingid, ordertype, orderstatus, orderfee, price, quantity, opendate)
VALUES
	(?, ?, ?,  ?, ?, ?, ?, ?, ?);
SELECT @@IDENTITY AS orderid;