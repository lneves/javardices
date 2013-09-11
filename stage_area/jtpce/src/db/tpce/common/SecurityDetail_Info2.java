package db.tpce.common;

public class SecurityDetail_Info2
{
	public final String co_name;
	public final String in_name;

	public SecurityDetail_Info2(String co_name, String in_name)
	{
		super();
		this.co_name = co_name;
		this.in_name = in_name;
	}

	@Override
	public String toString()
	{
		return String.format("SecurityDetail_Info2 {co_name=%s, in_name=%s}", co_name, in_name);
	}
}