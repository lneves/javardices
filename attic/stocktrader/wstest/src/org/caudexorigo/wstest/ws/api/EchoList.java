package org.caudexorigo.wstest.ws.api;

import org.caudexorigo.wstest.domain.TestNode;

public class EchoList
{
	private TestNode list;

	public TestNode getList()
	{
		return this.list;
	}

	public void setList(TestNode list)
	{
		this.list = list;
	}
}