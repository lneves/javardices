package org.caudexorigo.wstest.srv;

import org.caudexorigo.wstest.ws.xml.SoapEnvelope;

public interface SoapOperationHandler
{
	public SoapEnvelope handleMessage(SoapEnvelope soap_in);
}