package trade.ws;

import trade.domain.AccountProfileData;

public class UpdateAccountProfileResponse
{
	private AccountProfileData updateAccountProfileReturn;

	public AccountProfileData getUpdateAccountProfileReturn()
	{
		return updateAccountProfileReturn;
	}

	public void setUpdateAccountProfileReturn(AccountProfileData updateAccountProfileReturn)
	{
		this.updateAccountProfileReturn = updateAccountProfileReturn;
	}
}
