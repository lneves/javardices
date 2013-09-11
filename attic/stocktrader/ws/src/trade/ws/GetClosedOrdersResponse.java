package trade.ws;

import java.util.List;

import trade.domain.OrderData;

public class GetClosedOrdersResponse
{
	private List<OrderData> getClosedOrdersReturn;

	public List<OrderData> getClosedOrdersReturn()
	{
		return getClosedOrdersReturn;
	}

	public void setClosedOrdersReturn(List<OrderData> getClosedOrdersReturn)
	{
		this.getClosedOrdersReturn = getClosedOrdersReturn;
	}
}
