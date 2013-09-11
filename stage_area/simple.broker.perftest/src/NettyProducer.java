import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.caudexorigo.Shutdown;
import org.caudexorigo.concurrent.CustomExecutors;
import org.caudexorigo.concurrent.Sleep;
import org.caudexorigo.text.RandomStringUtils;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.types.NetAction;
import pt.com.broker.types.NetBrokerMessage;
import pt.com.broker.types.NetMessage;
import pt.com.broker.types.NetPublish;
import pt.com.broker.types.NetAction.ActionType;
import pt.com.broker.types.NetAction.DestinationType;

public class NettyProducer
{
	private static final Logger log = LoggerFactory.getLogger(NettyProducer.class);

	private static final Charset UTF8 = Charset.forName("UTF-8");

	private String host;
	private int port;
	private DestinationType dtype;
	private String destination;
	private final AtomicInteger counter = new AtomicInteger(0);
	private int mnr;
	private int wmnr;
	private long start;
	private boolean requireAck = true;
	private boolean isInWarmup = true;

	private static class NettyProducerHandler extends SimpleChannelUpstreamHandler
	{
		private final NettyProducer np;

		public NettyProducerHandler(NettyProducer producer)
		{
			this.np = producer;
		}

		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
		{
			System.out.println("NettyProducer.NettyProducerHandler.channelClosed()");
			super.channelClosed(ctx, e);
		}

		@Override
		public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception
		{
			if (e instanceof ChannelStateEvent && ((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS)
			{
				log.info(e.toString());
			}
			super.handleUpstream(ctx, e);
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		{
			// Echo back the received object to the client.
			// e.getChannel().write(e.getMessage());
			log.info(e.getMessage().toString());
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		{
			Shutdown.now(e.getCause());
		}
	}

	private void sendLoop(Channel channel, int msize, long len) throws InterruptedException
	{
		final String textMessage = RandomStringUtils.randomAlphanumeric(msize);
		NetBrokerMessage brokerMessage = new NetBrokerMessage(textMessage.getBytes(UTF8));
		NetMessage message = build(dtype, destination, brokerMessage);

		long start, stop;
		start = System.currentTimeMillis();

		long counter = 0;
		while (counter < len)
		{
			if (channel.isWritable())
			{

				if (counter % 5000 == 0)
				{
					long partial = System.currentTimeMillis();
					double elapsed = ((double) (partial - start)) / ((double) 1000);
					log.info(String.format("Sent %s messages in %.2f sec.", counter, elapsed));
				}
				channel.write(message);
				counter++;
			}
			else
			{
				Thread.sleep(0, 10);
			}
		}

		stop = System.currentTimeMillis();

		double duration = ((double) (stop - start)) / ((double) 1000);
		double rate = ((double) (len)) / duration;

		log.info(String.format("Total messages sent", counter));
		log.info(String.format("Total time: %.2f sec.", duration));
		log.info(String.format("Rate: %.2f msg/sec.", rate));
	}

	public static void main(String[] args)
	{
		try
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

			final NettyProducer producer = new NettyProducer();
			producer.host = tprop.getHost();
			producer.port = tprop.getPort();
			producer.dtype = tprop.getDestinationType();
			producer.destination = tprop.getDestination();
			producer.mnr = tprop.getNumMessages();
			producer.wmnr = tprop.getNumWarmupMessages();
			producer.requireAck = tprop.isAckRequired();
			producer.counter.set(0);

			// Configure the client.
			ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

			bootstrap.setOption("child.tcpNoDelay", false);
			bootstrap.setOption("child.keepAlive", true);
			bootstrap.setOption("child.receiveBufferSize", 256 * 1024);
			bootstrap.setOption("child.sendBufferSize", 256 * 1024);
			bootstrap.setOption("child.soLinger", 1);

			// Set up the pipeline factory.
			bootstrap.setPipelineFactory(new ChannelPipelineFactory()
			{
				public ChannelPipeline getPipeline() throws Exception
				{
					return Channels.pipeline(new BrokerDecoderRouter(), new BrokerEncoderRouter(), new NettyProducerHandler(producer));
				}
			});

			bootstrap.setOption("writeBufferHighWaterMark", 128 * 1024); // default=64K

			final CountDownLatch latch = new CountDownLatch(1);

			// Start the connection attempt.
			ChannelFuture cf = bootstrap.connect(new InetSocketAddress(producer.host, producer.port));

			cf.addListener(new ChannelFutureListener()
			{
				@Override
				public void operationComplete(ChannelFuture f) throws Exception
				{
					if (f.isSuccess())
					{
						latch.countDown();
					}
				}
			});

			latch.await();

			log.info("Is connected, will start test");

			if (tprop.getNumWarmupMessages() > 0)
			{
				log.info("Begin WarmUp");
				producer.sendLoop(cf.getChannel(), tprop.getMessageSize(), tprop.getNumWarmupMessages());
				log.info("End WarmUp");
				Sleep.time(1000);
			}

			log.info("Begin Test");
			producer.sendLoop(cf.getChannel(), tprop.getMessageSize(), tprop.getNumMessages());
			log.info("End Test");

			Sleep.time(1000);
			Shutdown.now();
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}

	private static NetMessage build(DestinationType dtype, String dname, NetBrokerMessage brokerMessage)
	{
		NetPublish publish = null;
		if (dtype == DestinationType.QUEUE)
		{
			publish = new NetPublish(dname, pt.com.broker.types.NetAction.DestinationType.QUEUE, brokerMessage);
		}
		else
		{
			publish = new NetPublish(dname, pt.com.broker.types.NetAction.DestinationType.TOPIC, brokerMessage);
		}

		NetAction action = new NetAction(ActionType.PUBLISH);
		action.setPublishMessage(publish);

		NetMessage message = new NetMessage(action, null);

		return message;
	}
}
