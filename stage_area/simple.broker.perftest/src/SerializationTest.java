import java.io.OutputStream;

import org.caudexorigo.concurrent.Sleep;
import org.caudexorigo.io.NullOutputStream;
import org.caudexorigo.text.RandomStringUtils;
import org.caudexorigo.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.codec.protobuf.ProtoBufBindingSerializer;
import pt.com.broker.codec.thrift.ThriftBindingSerializer;
import pt.com.broker.types.NetAction;
import pt.com.broker.types.NetBrokerMessage;
import pt.com.broker.types.NetMessage;
import pt.com.broker.types.NetNotification;
import pt.com.broker.types.NetPublish;
import pt.com.broker.types.NetAction.ActionType;

public class SerializationTest
{
	private static final ProtoBufBindingSerializer serializer = new ProtoBufBindingSerializer();
	//private static final ThriftBindingSerializer serializer = new ThriftBindingSerializer();

	private static final String MESSAGE_SOURCE = "MESSAGE_SOURCE";

	private static final String DEFAULT_REQUEST_SOURCE = "broker://agent.local/localhost/";

	private static final Logger log = LoggerFactory.getLogger(SerializationTest.class);

	public static void main(String[] args)
	{

		String payload = RandomStringUtils.randomAlphanumeric(128);
		NetBrokerMessage bmsg = new NetBrokerMessage(payload);

		NetPublish publish = new NetPublish("/perf/topic", pt.com.broker.types.NetAction.DestinationType.TOPIC, bmsg);

		NetAction action = new NetAction(ActionType.PUBLISH);
		action.setPublishMessage(publish);

		NetMessage nmsg = new NetMessage(action);

		byte[] publish_message = serializer.marshal(nmsg);
		
		
		for (int i = 0; i <10; i++)
		{
			doTest(publish_message, 500000);
			Sleep.time(1000);
		}
	}

	private static final void doTest(byte[] publish_message, int times)
	{
		
		long start, stop;
		start = System.currentTimeMillis();
		
		log.info("");
		log.info("********************************************");
		log.info("********************************************");
		log.info("***************  BEGIN TEST ****************");
		
		NullOutputStream dev_null = new NullOutputStream();
		for (int i = 0; i < times; i++)
		{

			NetMessage nmsg = serializer.unmarshal(publish_message);
			handlePublishMessage(nmsg, dev_null);

		}
		
		stop = System.currentTimeMillis();

		double duration = ((double) (stop - start)) / ((double) 1000);
		double rate = ((double) (times)) / duration;
		
		log.info(String.format("Total messages: ", times));
		log.info(String.format("Total time: %.2f sec.", duration));
		log.info(String.format("Rate: %.2f msg/sec.", rate));
		
		log.info("***************   END TEST  ****************");
		log.info("");

	}

	private static final void handlePublishMessage(NetMessage request, OutputStream out)
	{
		final String messageSource = requestSource(request);
		NetPublish publish = request.getAction().getPublishMessage();

		String actionId = publish.getActionId();
		String destination = publish.getDestination();

		if (!isValidDestination(destination))
		{
			log.error("isValidDestination");
			return;
		}

		if (StringUtils.contains(destination, "@"))
		{
			log.error("isValidDestination");
			return;
		}

		switch (publish.getDestinationType())
		{
		case TOPIC:
			NetMessage nmsgt = publishMessage(publish, messageSource);
			serializer.marshal(nmsgt, out);
			break;
		case QUEUE:
			NetMessage nmsgq = publishMessage(publish, messageSource);
			serializer.marshal(nmsgq, out);
			break;
		default:
			log.error("Unknown message type");
		}
	}

	private static final NetMessage publishMessage(final NetPublish np, final String messageSource)
	{
		StringBuilder sb_source = new StringBuilder();
		sb_source.append("dtype@");
		sb_source.append("agent.local");
		sb_source.append("://");
		sb_source.append(np.getDestination());
		sb_source.append("?app=");
		sb_source.append(messageSource);

		np.getMessage().addHeader("FROM", sb_source.toString());

		return buildNotification(np, "/dtype/perf");
	}

	private static final NetMessage buildNotification(NetPublish np, String subscriptionName)
	{
		String msg_id = MessageId.getMessageId();

		if (StringUtils.isBlank(np.getMessage().getMessageId()))
		{
			np.getMessage().setMessageId(msg_id);
		}

		long now = System.currentTimeMillis();
		if (np.getMessage().getTimestamp() == -1)
		{
			np.getMessage().setTimestamp(now);
		}

		if (np.getMessage().getExpiration() == -1)
		{
			np.getMessage().setExpiration(now + 50000);
		}

		NetNotification notification = new NetNotification(np.getDestination(), np.getDestinationType(), np.getMessage(), subscriptionName);

		NetAction action = new NetAction(NetAction.ActionType.NOTIFICATION);
		action.setNotificationMessage(notification);

		NetMessage message = new NetMessage(action);

		message.getHeaders().putAll(np.getMessage().getHeaders());

		return message;
	}

	private static final boolean isValidDestination(String destination)
	{
		return StringUtils.isNotBlank(destination);
	}

	public static String requestSource(NetMessage message)
	{
		String from = message.getHeaders().get("FROM");

		if (StringUtils.isNotBlank(from))
		{
			return from;
		}

		return DEFAULT_REQUEST_SOURCE;
	}

}
