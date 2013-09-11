SELECT
	holdingid
	, account_accountid
	, quote_symbol
	, purchaseprice
	, quantity
	, purchasedate
FROM
	holding
WHERE
	holdingid = ?;