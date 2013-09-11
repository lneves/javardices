package pt.sapo.wa;

import java.util.HashSet;
import java.util.Set;

import org.caudexorigo.builder.ReflectionToStringBuilder;
import org.caudexorigo.builder.ToStringStyle;

import pt.com.broker.types.NetAction.DestinationType;

public class SampleWaClient
{
	static Set<String> repeats = new HashSet<String>();

	static int minutes = 100;

	static int getMinutes()
	{
		return minutes;
	}

	static void incMinutes()
	{
		minutes++;
	}

	public static void main(String[] args)
	{
		// String host = "192.168.56.101";//"broker.bk.sapo.pt";
		// host="10.135.6.11";
		String host = "wa-staging";
		int port = 3323;
		DestinationType dtype = DestinationType.TOPIC;
		String dname = "/sapo/webanalytics/page-view/.*";

		WaHandler wa_handler = new WaHandler()
		{
			@Override
			public void onPageView(Event ev, Ack ack)
			{
				System.out.println(ReflectionToStringBuilder.toString(ev, ToStringStyle.MULTI_LINE_STYLE));
				// System.out.printf("visitorId: %s; url: %s%n", ev.getUserVisitorId(), ev.getContentPath());
				// System.out.printf("sourceType: %s; sourceKeywords: %s; sourceRef: %s%n", ev.getUserSourceType(), ev.getUserSourceKeywords(), ev.getUserSourceReferrer());
//				if (ev.getTotalTime() > 0 && ev.getTotalTime() > getMinutes() * 60 * 1000)
//				{
//					System.out.println(ev.getSiteKey() + "=>" + (ev.getTotalTime() - ev.getOnLoadTime()));
//					System.out.print("> MINUTES: " + getMinutes() + " ");
//					// System.out.print("" + ev.getServerTime() + ",");
//					// System.out.print("" + ev.getProcessingTime() + ",");
//					// System.out.print("" + ev.getOnLoadTime() + ",");
//					// System.out.print("" + ev.getTotalTime() + "\n");
//					System.out.println(ReflectionToStringBuilder.toString(ev, ToStringStyle.MULTI_LINE_STYLE));
//					incMinutes();
//				}
			}
		};

		WaConsumer wa_consumer = new WaConsumer(host, port, dtype, dname, wa_handler, AckRequired.NO);

		wa_consumer.start();
	}
}