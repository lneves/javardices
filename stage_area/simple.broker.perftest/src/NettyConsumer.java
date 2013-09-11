import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.caudexorigo.Shutdown;
import org.caudexorigo.concurrent.CustomExecutors;
import org.caudexorigo.concurrent.Sleep;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelEvent;
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

import pt.com.broker.types.NetAcknowledge;
import pt.com.broker.types.NetAction;
import pt.com.broker.types.NetBrokerMessage;
import pt.com.broker.types.NetMessage;
import pt.com.broker.types.NetNotification;
import pt.com.broker.types.NetSubscribe;
import pt.com.broker.types.NetAction.ActionType;
import pt.com.broker.types.NetAction.DestinationType;

public class NettyConsumer
{
	private static final Logger log = LoggerFactory.getLogger(NettyConsumer.class);

	private static final ScheduledExecutorService sched_exec = CustomExecutors.newScheduledThreadPool(1, "sched-exec");

	private String host;
	private int port;
	private DestinationType dtype;
	private String subscription;
	private final AtomicInteger counter = new AtomicInteger(0);
	private int mnr;
	private int wmnr;
	private long start;
	private boolean requireAck = true;
	private boolean isInWarmup = true;

	private static class NettyProducerHandler extends SimpleChannelUpstreamHandler
	{

		private final NetMessage netMessage;
		private final NettyConsumer nc;

		public NettyProducerHandler(NettyConsumer nc)
		{
			super();

			this.nc = nc;
			this.netMessage = build(nc.dtype, nc.subscription);
		}

		private NetMessage build(DestinationType dtype, String dname)
		{
			NetSubscribe subscribe = new NetSubscribe(dname, dtype);
			NetAction action = new NetAction(ActionType.SUBSCRIBE);
			action.setSubscribeMessage(subscribe);

			NetMessage message = new NetMessage(action, null);

			message.getHeaders().put("ACK_REQUIRED", Boolean.toString(nc.requireAck));

			return message;
		}

		private NetMessage buildAck(NetNotification notification)
		{
			NetBrokerMessage brkMsg = notification.getMessage();
			NetAcknowledge net_ack = new NetAcknowledge(notification.getDestination(), brkMsg.getMessageId());
			NetAction ack_action = new NetAction(ActionType.ACKNOWLEDGE);
			ack_action.setAcknowledgeMessage(net_ack);
			return new NetMessage(ack_action, null);
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
		public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		{
			log.info("NettyConsumer.NettyProducerHandler.channelConnected()");
			e.getChannel().write(netMessage);
			nc.start = System.currentTimeMillis();
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		{
			int count = nc.counter.incrementAndGet();

			if (nc.requireAck)
			{
				NetMessage ack_msg = buildAck(((NetMessage) e.getMessage()).getAction().getNotificationMessage());
				ctx.getChannel().write(ack_msg);
			}

			if (nc.isInWarmup && (count == nc.wmnr))
			{
				nc.start = System.currentTimeMillis();

				if (nc.wmnr > 0)
				{
					log.info("End WarmUp");
				}
				nc.counter.set(0);
				nc.isInWarmup = false;
				log.info("Begin Test");
				return;
			}

			if (count == nc.mnr)
			{
				long stop = System.currentTimeMillis();

				double duration = ((double) (stop - nc.start)) / ((double) 1000);
				double rate = ((double) (nc.mnr)) / duration;

				log.info("End Test");
				log.info(String.format("Total messages received: %s", count));
				log.info(String.format("Total time: %.2f sec.", duration));
				log.info(String.format("Rate: %.2f msg/sec.", rate));

				Thread t = new Thread()
				{
					public void run()
					{
						Sleep.time(1000);
						Shutdown.now();
					}
				};
				t.start();
			}

			if (count % 5000 == 0)
			{
				long partial = System.currentTimeMillis();
				double elapsed = ((double) (partial - nc.start)) / ((double) 1000);
				log.info(String.format("Received %s messages in %.2f sec.", count, elapsed));
			}

			// System.out.printf("===========================     [%s]#%s   =================================%n",
			// new Date(), counter.incrementAndGet());
			// System.out.printf("Destination: '%s'%n",
			// notification.getDestination());
			// System.out.printf("Subscription: '%s'%n",
			// notification.getSubscription());
			// System.out.printf("Payload: '%s'%n", new
			// String(notification.getMessage().getPayload()));
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		{
			log.warn("Unexpected exception from downstream.", e.getCause());
			e.getChannel().close();
		}
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

			final NettyConsumer consumer = new NettyConsumer();

			consumer.host = tprop.getHost();
			consumer.port = tprop.getPort();
			consumer.dtype = tprop.getDestinationType();
			consumer.subscription = tprop.getSubscription();
			consumer.mnr = tprop.getNumMessages();
			consumer.wmnr = tprop.getNumWarmupMessages();
			consumer.requireAck = tprop.isAckRequired();
			consumer.counter.set(0);

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
					return Channels.pipeline(new BrokerDecoderRouter(), new BrokerEncoderRouter(), new NettyProducerHandler(consumer));
				}
			});

			// Start the connection attempt.
			bootstrap.connect(new InetSocketAddress(consumer.host, consumer.port));

		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}
}