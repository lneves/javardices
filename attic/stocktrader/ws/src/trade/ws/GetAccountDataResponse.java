package trade.ws;

import trade.domain.AccountData;

public class GetAccountDataResponse
{
	private AccountData getAccountDataReturn;

	public AccountData getAccountDataReturn()
	{
		return getAccountDataReturn;
	}

	public void setAccountDataReturn(AccountData getAccountDataReturn)
	{
		this.getAccountDataReturn = getAccountDataReturn;
	}
}
