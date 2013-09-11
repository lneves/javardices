package org.caudexorigo.wstest.ws.api;

import org.caudexorigo.wstest.domain.COfTestStruct;

public class EchoStructResponse
{
	protected COfTestStruct echoStructResult;

	public COfTestStruct getEchoStructResult()
	{
		return this.echoStructResult;
	}

	public void setEchoStructResult(COfTestStruct echoStructResult)
	{
		this.echoStructResult = echoStructResult;
	}
}