package db.tpce.common;

public class Sector
{
	public final String sc_id;
	public final String sc_name;

	public Sector(String scId, String scName)
	{
		super();
		sc_id = scId;
		sc_name = scName;
	}

	@Override
	public String toString()
	{
		return String.format("Sector {sc_id=%s, sc_name=%s}", sc_id, sc_name);
	}
}