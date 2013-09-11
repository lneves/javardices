package db.tpce.common;

public class Company
{
	public final long co_id;

	public Company(long co_id)
	{
		super();
		this.co_id = co_id;
	}

	@Override
	public String toString()
	{
		return String.format("Company {co_id=%s}", co_id);
	}
}