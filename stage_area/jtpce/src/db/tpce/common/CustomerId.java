package db.tpce.common;

public class CustomerId
{
	public final long c_id;
	public final String c_tax_id;
	public final long min_ca_id;
	public final int count_ca;

	public CustomerId(long c_id, String c_tax_id, long min_ca_id, int count_ca)
	{
		super();
		this.c_id = c_id;
		this.c_tax_id = c_tax_id;
		this.min_ca_id = min_ca_id;
		this.count_ca = count_ca;
	}

	@Override
	public String toString()
	{
		return String.format("CustomerId {c_id=%s, c_tax_id=%s, min_ca_id=%s, count_ca=%s}", c_id, c_tax_id, min_ca_id, count_ca);
	}

}