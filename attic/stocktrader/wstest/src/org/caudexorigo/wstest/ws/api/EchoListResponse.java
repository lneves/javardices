package org.caudexorigo.wstest.ws.api;

import org.caudexorigo.wstest.domain.TestNode;

public class EchoListResponse
{
	protected TestNode echoListResult;

	public TestNode getEchoListResult()
	{
		return this.echoListResult;
	}

	public void setEchoListResult(TestNode echoListResult)
	{
		this.echoListResult = echoListResult;
	}
}