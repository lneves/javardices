UPDATE
	account
SET
	logoutcount = (logoutcount + 1)
WHERE  profile_userid = ?;