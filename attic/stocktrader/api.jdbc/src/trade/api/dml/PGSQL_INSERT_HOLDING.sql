INSERT INTO holding
	(account_accountid, quote_symbol, purchaseprice, quantity, purchasedate)
VALUES
	(?, ?, ?, ?, CURRENT_TIMESTAMP)
RETURNING holdingid;