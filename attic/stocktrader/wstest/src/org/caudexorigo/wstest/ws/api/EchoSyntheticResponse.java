package org.caudexorigo.wstest.ws.api;

import org.caudexorigo.wstest.domain.Synthetic;

public class EchoSyntheticResponse
{
	protected Synthetic echoSyntheticResult;

	public Synthetic getEchoSyntheticResult()
	{
		return this.echoSyntheticResult;
	}

	public void setEchoSyntheticResult(Synthetic echoSyntheticResult)
	{
		this.echoSyntheticResult = echoSyntheticResult;
	}
}