package trade.ws;

import java.math.BigDecimal;

public class Register
{
	private String userID;
	private String password;
	private String fullname;
	private String address;
	private String email;
	private String creditcard;
	private BigDecimal openBalance;

	public String getUserId()
	{
		return userID;
	}

	public void setUserId(String userID)
	{
		this.userID = userID;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getFullname()
	{
		return fullname;
	}

	public void setFullname(String fullname)
	{
		this.fullname = fullname;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCreditcard()
	{
		return creditcard;
	}

	public void setCreditcard(String creditcard)
	{
		this.creditcard = creditcard;
	}

	public BigDecimal getOpenBalance()
	{
		return openBalance;
	}

	public void setOpenBalance(BigDecimal openBalance)
	{
		this.openBalance = openBalance;
	}
}
