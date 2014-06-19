package wstest.ws.xml;

public class SoapFault
{
	public String faultCode;

	public String faultString;

	public String detail;

	public SoapFault()
	{
		detail = "";
		faultCode = "";
		faultString = "";
	}

	@Override
	public String toString()
	{
		return "SoapFault [faultCode=" + faultCode + ", faultString=" + faultString + "]";
	}
}
