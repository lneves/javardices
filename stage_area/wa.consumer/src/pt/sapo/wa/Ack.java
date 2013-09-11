package pt.sapo.wa;

import pt.com.broker.client.BrokerClient;
import pt.com.broker.types.NetNotification;

public class Ack
{
	private final BrokerClient bk;
	private final NetNotification notification;
	private final boolean isAckRequired;
	private boolean isAcked = false;

	protected Ack(BrokerClient bk, NetNotification notification, boolean isAckRequired)
	{
		super();
		this.bk = bk;
		this.notification = notification;
		this.isAckRequired = isAckRequired;
	}

	public void doIt() throws Throwable
	{
		if (isAckRequired)
		{
			bk.acknowledge(notification);
			isAcked = true;
		}
	}

	protected boolean isAcked()
	{
		return isAcked;
	}
}