package pt.sapo.wa;

import org.caudexorigo.Shutdown;
import org.caudexorigo.builder.ReflectionToStringBuilder;
import org.caudexorigo.builder.ToStringStyle;
import org.caudexorigo.io.UnsynchronizedByteArrayInputStream;

public class SerializerTest
{
	public static void main(String[] args)
	{
		try
		{
			String xml = "<event type=\"page-view\" key=\"2fa5f304-ba35-41a9-be89-2c9b41584d8f\" timestamp=\"1348746289346\">  <user>    <ip>127.0.0.1</ip>    <global-visitor-id new-id=\"true\">4547771741347270876</global-visitor-id>    <visitor-id new-id=\"false\">3719358421347270876</visitor-id>    <visit-id new-id=\"false\">3247446741348745016</visit-id>    <visit-start>1348745015979</visit-start>    <visitor-type>returning</visitor-type>    <source-type>direct</source-type>    <source-keywords>(none)</source-keywords>    <source-referrer>(none)</source-referrer>  </user>  <content>    <host>www.frogland.pt</host>    <path>/direct.html</path>    <title>Teste Directo</title>    <has-ti>true</has-ti>    <ti-server-time>3</ti-server-time>    <ti-processing-time>37</ti-processing-time>    <ti-total-time>44</ti-total-time>  </content>  <user-agent>    <ua-string>Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1</ua-string>    <ua-type>Browser</ua-type>    <os-name>Linux</os-name>    <os-version>Linux</os-version>    <browser-name>Chrome</browser-name>    <browser-version>Chrome 21.0.1180.89</browser-version>    <charset>iso-8859-1</charset>    <screen-resolution>1680x1050</screen-resolution>    <language>en-us</language>    <color-depth>24</color-depth>    <java-enabled>true</java-enabled>    <flash-version>11.3 r31</flash-version>  </user-agent>  <geo>    <country id=\"\"></country>    <district id=\"\"></district>    <municipality id=\"\"></municipality>    <parish id=\"\"></parish>    <latitude>0.0</latitude>    <longitude>0.0</longitude>  </geo>  <extra>    <goal>(none)</goal>  </extra></event>";

			Event ev = EventSerializer.fromXml(new UnsynchronizedByteArrayInputStream(xml.getBytes()));

			System.out.println(ReflectionToStringBuilder.toString(ev, ToStringStyle.MULTI_LINE_STYLE));

		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}
}