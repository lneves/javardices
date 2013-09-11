package trade.srv;

import trade.xml.SoapEnvelope;

public interface SoapOperationHandler
{
	public SoapEnvelope handleMessage(SoapEnvelope soap_in);
}