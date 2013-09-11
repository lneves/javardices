UPDATE accountprofile
SET 
	email = ?
	, fullname = ?
	, password = ?
	, salt = ?
	, address = ?
	, creditcard = ?
WHERE
	userid = ?
