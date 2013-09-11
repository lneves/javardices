package trade.domain;

import java.math.BigDecimal;
import java.util.Date;

public class AccountData
{
	private int accountId;
	private int loginCount;
	private int logoutCount;
	private Date lastLogin;
	private Date creationDate;
	private BigDecimal balance;
	private BigDecimal openBalance;
	private String profileId;

	public int getAccountId()
	{
		return accountId;
	}

	public BigDecimal getBalance()
	{
		return balance;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public Date getLastLogin()
	{
		return lastLogin;
	}

	public int getLoginCount()
	{
		return loginCount;
	}

	public int getLogoutCount()
	{
		return logoutCount;
	}

	public BigDecimal getOpenBalance()
	{
		return openBalance;
	}

	public String getProfileId()
	{
		return profileId;
	}

	public void setAccountId(int accountId)
	{
		this.accountId = accountId;
	}

	public void setBalance(BigDecimal balance)
	{
		this.balance = balance;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public void setLastLogin(Date lastLogin)
	{
		this.lastLogin = lastLogin;
	}

	public void setLoginCount(int loginCount)
	{
		this.loginCount = loginCount;
	}

	public void setLogoutCount(int logoutCount)
	{
		this.logoutCount = logoutCount;
	}

	public void setOpenBalance(BigDecimal openBalance)
	{
		this.openBalance = openBalance;
	}

	public void setProfileId(String profileId)
	{
		this.profileId = profileId;
	}

	@Override
	public String toString()
	{
		return String.format("AccountData [accountId=%s, loginCount=%s, logoutCount=%s, lastLogin=%s, creationDate=%s, balance=%s, openBalance=%s, profileId=%s]", accountId, loginCount, logoutCount, lastLogin, creationDate, balance, openBalance, profileId);
	}
}