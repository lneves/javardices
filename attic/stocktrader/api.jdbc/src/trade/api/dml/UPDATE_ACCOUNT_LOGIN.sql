UPDATE
	account
SET
	logincount = (logincount + 1)
	, lastlogin = CURRENT_TIMESTAMP
WHERE 
	profile_userid = ?;