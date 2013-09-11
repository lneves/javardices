package trade.ws;

import trade.domain.AccountData;

public class LoginResponse
{
	private AccountData loginReturn;

	public AccountData getLoginReturn()
	{
		return loginReturn;
	}

	public void setLoginReturn(AccountData loginReturn)
	{
		this.loginReturn = loginReturn;
	}
}
