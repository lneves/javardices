package db.tpce.output;

import java.util.List;

import db.bench.output.TxOutput;
import db.tpce.common.BrokerVolume;

public class TBrokerVolumeFrame1Output extends TxOutput
{
	public final List<BrokerVolume> lst_broker_volume;
	public final int list_len;

	public TBrokerVolumeFrame1Output(int status, List<BrokerVolume> lst_broker_volume)
	{
		super(status);
		this.lst_broker_volume = lst_broker_volume;
		list_len = lst_broker_volume.size();
	}

	@Override
	public String toString()
	{
		return String.format("TBrokerVolumeFrame1Output [%nlst_broker_volume=%s, list_len=%s%n, status=%s%n, tx_time=%s%n]", lst_broker_volume, list_len, status, ((double) tx_time / 1000000.0));
	}
}