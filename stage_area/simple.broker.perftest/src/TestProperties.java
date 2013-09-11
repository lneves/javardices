import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.caudexorigo.Shutdown;
import org.caudexorigo.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.types.NetAction.DestinationType;

public class TestProperties
{
	private static final Logger log = LoggerFactory.getLogger(TestProperties.class);

	private static final String DEFAULT_PROPERTIES_FILE_NAME = "perf.properties";

	private final int numMessages;
	private final int numWarmupMessages;
	private final int messageSize;
	private final DestinationType destinationType;
	private final String destination;
	private final String subscription;
	private final String host;
	private final int port;
	private final boolean tcpNoDelay;
	private final boolean autoAck;
	private final boolean ackRequired;
	private final boolean useSendAcks;

	public TestProperties()
	{
		this(DEFAULT_PROPERTIES_FILE_NAME);
	}

	public TestProperties(String fileName)
	{
		super();

		if (StringUtils.isBlank(fileName))
		{
			fileName = DEFAULT_PROPERTIES_FILE_NAME;
		}

		Properties props = null;

		InputStream in_stream = null;

		try
		{
			try
			{
				in_stream = new FileInputStream(fileName);

				props = new Properties();

				props.load(in_stream);
			}
			finally
			{
				if (in_stream != null)
				{
					in_stream.close();
				}
			}
		}
		catch (Throwable t)
		{
			log.error("Fatal error initializing test properties");
			Shutdown.now(t);
		}

		numMessages = Integer.valueOf(props.getProperty("num-messages", "1000000"));
		numWarmupMessages = Integer.valueOf(props.getProperty("num-warmup-messages", "200000"));
		messageSize = Integer.valueOf(props.getProperty("message-size", "128"));
		destinationType = DestinationType.valueOf(props.getProperty("destination-type", "TOPIC"));
		destination = props.getProperty("destination", "/test/perf");
		subscription = props.getProperty("subscription", "/test/perf");
		host = props.getProperty("host", "localhost");
		port = Integer.valueOf(props.getProperty("port", "3323"));
		tcpNoDelay = Boolean.valueOf(props.getProperty("tcp-no-delay"));
		autoAck = Boolean.valueOf(props.getProperty("true"));
		ackRequired = Boolean.valueOf(props.getProperty("ack-required"));
		useSendAcks = Boolean.valueOf(props.getProperty("use-send-acks"));
	}

	public int getNumMessages()
	{
		return numMessages;
	}

	public int getNumWarmupMessages()
	{
		return numWarmupMessages;
	}

	public int getMessageSize()
	{
		return messageSize;
	}

	public DestinationType getDestinationType()
	{
		return destinationType;
	}

	public String getDestination()
	{
		return destination;
	}

	public String getSubscription()
	{
		return subscription;
	}

	public String getHost()
	{
		return host;
	}

	public int getPort()
	{
		return port;
	}

	public boolean isTcpNoDelay()
	{
		return tcpNoDelay;
	}

	public boolean isAutoAck()
	{
		return autoAck;
	}

	public boolean isAckRequired()
	{
		return ackRequired;
	}

	public boolean isUseSendAcks()
	{
		return useSendAcks;
	}

	@Override
	public String toString()
	{
		return "\nTestProperties [\n ackRequired=" + ackRequired + ",\n destination=" + destination + ",\n subscription=" + subscription + ",\n autoAck=" + autoAck + ",\n destinationType=" + destinationType + ",\n host=" + host + ",\n messageSize=" + messageSize + ",\n numMessages=" + numMessages + ",\n numWarmupMessages=" + numWarmupMessages + ",\n port=" + port + ",\n tcpNoDelay=" + tcpNoDelay + ",\n useSendAcks=" + useSendAcks + "\n]";
	}

}