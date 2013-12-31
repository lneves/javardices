package wstest.srv;

import wstest.srv.actors.COfTestStruct;
import wstest.srv.actors.Order;
import wstest.srv.actors.Synthetic;
import wstest.srv.actors.TestNode;

public class WSTestImpl implements IWSTest
{
	@Override
	public TestNode echoList(TestNode list)
	{
		return list;
	}

	@Override
	public COfTestStruct echoStruct(COfTestStruct array)
	{
		return array;
	}

	@Override
	public Synthetic echoSynthetic(Synthetic synth)
	{
		return synth;
	}

	@Override
	public void echoVoid()
	{
		// do nothing
	}

	@Override
	public Order getOrder(int orderId, int customerId, int messageSize)
	{
		OrderBL bl = new OrderBL();
		return bl.getOrder(orderId, customerId, messageSize);
	}
}
