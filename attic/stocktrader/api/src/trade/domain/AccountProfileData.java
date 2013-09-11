package trade.domain;

public class AccountProfileData
{
	private String userId;
	private String password;
	private String fullName;
	private String address;
	private String email;
	private String creditCard;
	private String salt;

	public String getAddress()
	{
		return address;
	}

	public String getCreditCard()
	{
		return creditCard;
	}

	public String getEmail()
	{
		return email;
	}

	public String getFullName()
	{
		return fullName;
	}

	public String getPassword()
	{
		return password;
	}

	public String getSalt()
	{
		return salt;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public void setCreditCard(String creditCard)
	{
		this.creditCard = creditCard;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setSalt(String salt)
	{
		this.salt = salt;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return String.format("AccountProfileData [userId=%s, password=%s, fullName=%s, address=%s, email=%s, creditCard=%s, salt=%s]", userId, password, fullName, address, email, creditCard, salt);
	}
}