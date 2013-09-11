INSERT INTO account
	(profile_userid, openbalance, balance, creationdate, lastlogin, logincount, logoutcount)
VALUES
	(?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0)
RETURNING accountid;