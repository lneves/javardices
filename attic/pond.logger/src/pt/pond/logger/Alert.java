package pt.pond.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.Date;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.text.DateUtil;
import org.caudexorigo.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.client.BrokerClient;
import pt.com.broker.types.NetBrokerMessage;

public class Alert
{
	private static final Logger log = LoggerFactory.getLogger(Alert.class);
	private static String hostname;

	static
	{
		try
		{
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		}
		catch (Throwable t)
		{
			hostname = "unknown";
		}
	}

	public static void publishError(BrokerClient bk, String applicationName, Throwable error)
	{
		try
		{
			Throwable rootCause = ErrorAnalyser.findRootCause(error);

			log.error(rootCause.getMessage());

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			rootCause.printStackTrace(pw);
			pw.flush();
			sw.flush();
			String stackTrace = sw.toString();

			StringBuilder sb = new StringBuilder();
			sb.append("<Exception>\n");
			sb.append("<ApplicationName>" + StringEscapeUtils.escapeXml(applicationName) + "</ApplicationName>\n");
			sb.append("<MachineName>" + StringEscapeUtils.escapeXml(hostname) + "</MachineName>\n");
			sb.append("<Timestamp>" + DateUtil.formatISODate(new Date()) + "</Timestamp>\n");
			sb.append("<Type>" + rootCause.getClass().getName() + "</Type>\n");
			sb.append("<Message>" + StringEscapeUtils.escapeXml(rootCause.getMessage()) + "</Message>\n");
			sb.append("<Detail>\n");
			sb.append(StringEscapeUtils.escapeXml(rootCause.getMessage()));
			sb.append("\n");
			sb.append("\n");
			sb.append(StringEscapeUtils.escapeXml(stackTrace));
			sb.append("</Detail>\n");
			sb.append("</Exception>\n");

			NetBrokerMessage brokerMessage = new NetBrokerMessage(sb.toString());

			bk.publishMessage(brokerMessage, "/sapo/services/exception");
		}
		catch (Throwable e)
		{
			log.error(e.getMessage(), e);
		}
	}
}