SELECT
	accountid
	, profile_userid
	, balance
	, openbalance
	, creationdate
	, lastlogin
	, logincount
	, logoutcount
FROM
	account
WHERE
	profile_userid = ?;