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
	account_accountid IN (SELECT accountid FROM account WHERE profile_userid = ?)
ORDER BY
	holdingid DESC;