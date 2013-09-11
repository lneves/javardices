package org.caudexorigo.wstest.ws.xml;

public class SoapEnvelope
{
	public SoapBody body;
	public SoapHeader header;

	public SoapEnvelope()
	{
		body = new SoapBody();
	}
}