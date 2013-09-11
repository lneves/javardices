package pt.sapo.wa;

import java.io.InputStream;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.Shutdown;
import org.caudexorigo.io.UnsynchronizedByteArrayInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.client.BrokerClient;
import pt.com.broker.client.messaging.BrokerListener;
import pt.com.broker.types.NetAction.DestinationType;
import pt.com.broker.types.NetBrokerMessage;
import pt.com.broker.types.NetNotification;
import pt.com.broker.types.NetSubscribe;

public class WaConsumer implements BrokerListener
{
	private static Logger log = LoggerFactory.getLogger(WaConsumer.class);

	private String host;
	private int port;
	private DestinationType dtype;
	private String dname;
	private BrokerClient bk;
	private WaHandler wa_handler;
	private final boolean ack_required;

	public WaConsumer(String host, int port, DestinationType dtype, String dname, WaHandler wa_handler, AckRequired ack_required)
	{
		super();

		try
		{
			// Verify if the Aalto parser is in the classpath
			Class.forName("com.fasterxml.aalto.stax.InputFactoryImpl").newInstance();
			Class.forName("com.fasterxml.aalto.stax.OutputFactoryImpl").newInstance();
			Class.forName("com.fasterxml.aalto.stax.EventFactoryImpl").newInstance();

			// If we made it here without errors set Aalto as our StaX parser
			System.setProperty("javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
			System.setProperty("javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
			System.setProperty("javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
		}
		catch (Throwable t)
		{
			log.warn("Aalto was not found in the classpath, will fallback to use the native parser");
		}

		this.host = host;
		this.port = port;
		this.dtype = dtype;
		this.dname = dname;
		this.wa_handler = wa_handler;
		this.ack_required = (dtype == DestinationType.TOPIC) ? false : ack_required.get();
	}

	public void start()
	{
		try
		{
			bk = new BrokerClient(host, port);
			NetSubscribe subscribe = new NetSubscribe(dname, dtype);

			if (!ack_required)
			{
				subscribe.addHeader("ACK_REQUIRED", "false");
			}

			bk.addAsyncConsumer(subscribe, this);
		}
		catch (Throwable t)
		{
			bk = null;
			Shutdown.now(t);
		}
	}

	@Override
	public void onMessage(NetNotification notification)
	{
		Event ev = null;
		try
		{
			NetBrokerMessage bk_msg = notification.getMessage();
			InputStream payload = new UnsynchronizedByteArrayInputStream(bk_msg.getPayload());
			ev = EventSerializer.fromXml(payload);
			Ack ack = new Ack(bk, notification, ack_required);
			wa_handler.onPageView(ev, ack);

			if (ack_required && !ack.isAcked())
			{
				log.warn("Message acknowledgement is required but it wasn't sent");
			}
		}
		catch (Throwable t)
		{
			Throwable root = ErrorAnalyser.findRootCause(t);
			ev = new Event();
			ev.setInError(true);
			ev.setErrorMessage(root.getMessage());
		}
	}

	@Override
	public boolean isAutoAck()
	{
		return false;
	}
}