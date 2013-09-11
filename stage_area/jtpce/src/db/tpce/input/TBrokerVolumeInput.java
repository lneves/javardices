package db.tpce.input;

import java.util.Arrays;

public class TBrokerVolumeInput
{
	public String[] broker_list;
	public String sector_name;

	@Override
	public String toString()
	{
		return String.format("TBrokerVolumeInput [%nbroker_list={%s}%n, sector_name=%s%n]", Arrays.toString(broker_list), sector_name);
	}
}