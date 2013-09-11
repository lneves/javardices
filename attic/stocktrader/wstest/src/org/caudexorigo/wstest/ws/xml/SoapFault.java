package org.caudexorigo.wstest.ws.xml;

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
}