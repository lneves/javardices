package trade.datagen;

public class GenAccount
{
	private final String firstName;
	private final String lastName;

	private final String domain;

	public GenAccount(String firstName, String lastName, String domain)
	{
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.domain = domain;
	}

	public String getFullName()
	{
		return String.format("%s %s", firstName, lastName);
	}

	public String getEmail()
	{
		return String.format("%s.%s@%s", firstName.toLowerCase(), lastName.toLowerCase(), domain);
	}
}