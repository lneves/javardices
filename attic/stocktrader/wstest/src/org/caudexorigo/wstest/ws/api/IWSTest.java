package org.caudexorigo.wstest.ws.api;

import org.caudexorigo.wstest.domain.COfTestStruct;
import org.caudexorigo.wstest.domain.Order;
import org.caudexorigo.wstest.domain.Synthetic;
import org.caudexorigo.wstest.domain.TestNode;

public interface IWSTest
{
	public void echoVoid();

	public COfTestStruct echoStruct(COfTestStruct array);

	public TestNode echoList(TestNode list);

	public Synthetic echoSynthetic(Synthetic synth);

	public Order getOrder(int orderId, int customerId, int messageSize);
}