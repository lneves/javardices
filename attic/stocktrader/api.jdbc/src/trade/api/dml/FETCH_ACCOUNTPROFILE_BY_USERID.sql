SELECT
	userid
	, email
	, password
	, salt
	, fullname
	, address
	, creditcard
FROM
	accountprofile
WHERE
	userid = ?;