package sample;
public class User
{
	private final String user_first_name;
	private final String user_last_name;

	public User(String userFirstName, String userLastName)
	{
		super();
		user_first_name = userFirstName;
		user_last_name = userLastName;
	}

	public String getUser_first_name()
	{
		return user_first_name;
	}

	public String getUser_last_name()
	{
		return user_last_name;
	}

	@Override
	public String toString()
	{
		return "User [user_first_name=" + user_first_name + ", user_last_name=" + user_last_name + "]";
	}

}
