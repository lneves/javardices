package db.tpce.common;

public class Broker
{
	// store only static broker information
	public final long b_id;
	public final String b_st_id;
	public final String b_name;

	public Broker(long b_id, String b_st_id, String b_name)
	{
		super();
		this.b_id = b_id;
		this.b_st_id = b_st_id;
		this.b_name = b_name;
	}

	@Override
	public String toString()
	{
		return String.format("Broker {b_id=%s, b_name=%s, b_st_id=%s}", b_id, b_name, b_st_id);
	}
}
