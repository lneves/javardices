package trade.ws;

import trade.domain.AccountData;

public class RegisterResponse
{
	private AccountData registerReturn;

	public AccountData getRegisterReturn()
	{
		return registerReturn;
	}

	public void setRegisterReturn(AccountData registerReturn)
	{
		this.registerReturn = registerReturn;
	}
}
