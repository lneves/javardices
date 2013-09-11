package org.caudexorigo.twitter.social;

import org.caudexorigo.Shutdown;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.client.BrokerClient;
import pt.com.broker.types.NetBrokerMessage;
import pt.com.broker.types.NetPong;

public class BrokerHandler
{
	private static final Logger log = LoggerFactory.getLogger(BrokerHandler.class);
	private final BrokerClient bk;


	public BrokerHandler(String host, int port)
	{
		super();
		bk = SafeBrokerBuilder.build(host, port);
	}

	public void publish(String message, String broker_destination)
	{
		NetBrokerMessage brokerMessage = new NetBrokerMessage(message);
		bk.publishMessage(brokerMessage, broker_destination);
	}

	public void ping()
	{
		try
		{
			NetPong pong = bk.checkStatus();
			log.info("Broker ping, got actionId: '{}'", pong.getActionId());
		}
		catch (Throwable e)
		{
			Shutdown.now(e);
		}
	}
}

final class SafeBrokerBuilder
{
	protected static final BrokerClient build(String host, int port)
	{
		try
		{
			return new BrokerClient(host, port);
		}
		catch (Throwable e)
		{
			Shutdown.now(e);
			throw new RuntimeException(e);
		}
	}
}
