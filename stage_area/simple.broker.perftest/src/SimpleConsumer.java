import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.caudexorigo.Shutdown;
import org.caudexorigo.concurrent.CustomExecutors;
import org.caudexorigo.concurrent.Sleep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.client.BrokerClient;
import pt.com.broker.client.messaging.BrokerListener;
import pt.com.broker.client.sample.Consumer;
import pt.com.broker.types.NetAction.DestinationType;
import pt.com.broker.types.NetNotification;
import pt.com.broker.types.NetSubscribe;

public class SimpleConsumer implements BrokerListener
{
	private static final Logger log = LoggerFactory.getLogger(Consumer.class);
	private static final ScheduledExecutorService shed_exec = CustomExecutors.newScheduledThreadPool(1, "test-finalizer");

	private int mnr;
	private int wmnr;
	private int counter;
	private long start;
	private boolean isInWarmup = true;
	private boolean isAutoAck;

	private final BrokerClient bk;
	private final String dname;
	private final DestinationType dtype;

	public SimpleConsumer(BrokerClient bk, String dname, DestinationType dtype)
	{
		this.bk = bk;
		this.dname = dname;
		this.dtype = dtype;
	}

	public void setAutoAck(boolean isAutoAck)
	{
		this.isAutoAck = isAutoAck;
	}

	@Override
	public boolean isAutoAck()
	{
		return this.isAutoAck;
	}

	@Override
	public void onMessage(NetNotification notification)
	{
		counter++;

		if (isInWarmup && (counter == wmnr))
		{
			start = System.currentTimeMillis();

			if (wmnr > 0)
			{
				log.info("End WarmUp");
			}
			counter = 0;
			isInWarmup = false;
			log.info("Begin Test");
		}

		if (counter == mnr)
		{
			final long stop = System.currentTimeMillis();

			final double duration = ((double) (stop - start)) / ((double) 1000);
			final double rate = ((double) (mnr)) / duration;

			Runnable shutdown = new Runnable()
			{
				public void run()
				{
					try
					{
						bk.unsubscribe(dtype, dname);
						bk.close();
						Sleep.time(1000);

						log.info("End Test");
						log.info(String.format("Total messages received: %s", counter));
						log.info(String.format("Total time: %.2f sec.", duration));
						log.info(String.format("Rate: %.2f msg/sec.", rate));

						Shutdown.now();
					}
					catch (Throwable e)
					{
						Shutdown.now(e);
					}
				}
			};
			shed_exec.schedule(shutdown, 1, TimeUnit.SECONDS);

		}

		if (counter % 5000 == 0)
		{
			long partial = System.currentTimeMillis();
			double elapsed = ((double) (partial - start)) / ((double) 1000);
			log.info(String.format("Received %s messages in %.2f sec.", counter, elapsed));
		}
	}

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

		BrokerClient bk = new BrokerClient(tprop.getHost(), tprop.getPort(), "tcp://mycompany.com/mysniffer");

		SimpleConsumer consumer = new SimpleConsumer(bk, tprop.getDestination(), tprop.getDestinationType());

		consumer.mnr = tprop.getNumMessages();
		consumer.wmnr = tprop.getNumWarmupMessages();
		consumer.counter = 0;

		NetSubscribe subscribe = new NetSubscribe(tprop.getDestination(), tprop.getDestinationType());

		if (!tprop.isAckRequired())
		{
			subscribe.addHeader("ACK_REQUIRED", "false");
		}

		consumer.setAutoAck(tprop.getDestinationType() != DestinationType.TOPIC);

		bk.addAsyncConsumer(subscribe, consumer);

		log.info("Listening...");

		consumer.start = System.currentTimeMillis();

		if (consumer.wmnr > 0)
		{
			log.info("Begin WarmUp");
		}
	}
}