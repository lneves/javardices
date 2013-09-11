import org.caudexorigo.Shutdown;
import org.caudexorigo.concurrent.Sleep;
import org.caudexorigo.text.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.client.BrokerClient;
import pt.com.broker.types.Headers;
import pt.com.broker.types.NetAction.DestinationType;
import pt.com.broker.types.NetBrokerMessage;
import pt.com.broker.types.NetProtocolType;

public class SimpleProducer
{
	private static final Logger log = LoggerFactory.getLogger(SimpleProducer.class);

	private final TestProperties tprop;
	private BrokerClient bk;

	public static void main(String[] args) throws Throwable
	{
		TestProperties tprop;
		if (args.length > 0)
		{
			final String fileName = args[0];
			tprop = new TestProperties(fileName);
		}
		else
		{
			tprop = new TestProperties();
		}

		log.info(tprop.toString());

		SimpleProducer producer = new SimpleProducer(tprop);

		if (tprop.getNumWarmupMessages() > 0)
		{
			log.info("Begin WarmUp");
			producer.sendLoop(tprop.getMessageSize(), tprop.getNumWarmupMessages());
			log.info("End WarmUp");
			Sleep.time(1000);
		}

		log.info("Begin Test");
		producer.sendLoop(tprop.getMessageSize(), tprop.getNumMessages());

		log.info("End Test");

		Sleep.time(1000);
		Shutdown.now();
	}

	public SimpleProducer(TestProperties tprop)
	{
		super();
		this.tprop = tprop;

		try
		{
			bk = new BrokerClient(tprop.getHost(), tprop.getPort(), "tcp://mycompany.com/mypublisher", NetProtocolType.PROTOCOL_BUFFER);
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}

	private void sendLoop(int msize, long len) throws Throwable
	{
		final String msg = RandomStringUtils.randomAlphanumeric(msize);

		NetBrokerMessage brokerMessage = new NetBrokerMessage(msg.getBytes("UTF-8"));

		long start, stop;
		start = System.currentTimeMillis();

		long counter = 0;
		while (counter++ < len)
		{

			if (counter % 5000 == 0)
			{
				long partial = System.currentTimeMillis();
				double elapsed = ((double) (partial - start)) / ((double) 1000);
				log.info(String.format("Sent %s messages in %.2f sec.", counter, elapsed));
			}
			if (tprop.getDestinationType() == DestinationType.QUEUE)
			{
				bk.enqueueMessage(brokerMessage, tprop.getDestination());
			}
			else
			{
				bk.publishMessage(brokerMessage, tprop.getDestination());
			}
		}

		stop = System.currentTimeMillis();

		double duration = ((double) (stop - start)) / ((double) 1000);
		double rate = ((double) (len)) / duration;

		log.info(String.format("Total messages sent", counter));
		log.info(String.format("Total time: %.2f sec.", duration));
		log.info(String.format("Rate: %.2f msg/sec.", rate));
	}
}