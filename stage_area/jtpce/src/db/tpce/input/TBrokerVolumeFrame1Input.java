package db.tpce.input;

import java.util.Arrays;

public class TBrokerVolumeFrame1Input
{
	public String[] broker_list;
	public String sector_name;

	@Override
	public String toString()
	{
		return String.format("TBrokerVolumeFrame1Input [%nbroker_list={%s}%n, sector_name=%s%n]", Arrays.toString(broker_list), sector_name);
	}
}