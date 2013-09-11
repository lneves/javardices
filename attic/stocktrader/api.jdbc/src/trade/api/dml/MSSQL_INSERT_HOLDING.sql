SET NOCOUNT ON;
INSERT INTO holding
	(account_accountid, quote_symbol, purchaseprice, quantity, purchasedate)
VALUES
	(?, ?, ?, ?, CURRENT_TIMESTAMP);
SELECT @@IDENTITY AS holdingid;