package db.tpce.common;

public class BrokerVolume
{
	public final String broker_name;
	public final long volume;

	public BrokerVolume(String broker_name, long volume)
	{
		super();
		this.broker_name = broker_name;
		this.volume = volume;
	}

	@Override
	public String toString()
	{
		return String.format("BrokerVolume {broker_name=%s, volume=%s}", broker_name, volume);
	}
}