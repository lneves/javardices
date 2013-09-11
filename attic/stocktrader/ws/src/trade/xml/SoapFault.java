package trade.xml;

public class SoapFault
{
	public String faultCode;
	public String faultString;
	public String detail;

	public SoapFault()
	{
		detail = "";
		faultCode = "soap:Server";
		faultString = "";
	}
}