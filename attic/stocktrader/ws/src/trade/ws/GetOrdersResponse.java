package trade.ws;

import java.util.List;

import trade.domain.OrderData;

public class GetOrdersResponse
{
	private List<OrderData> getOrdersReturn;

	public List<OrderData> getOrdersReturn()
	{
		return getOrdersReturn;
	}

	public void setOrdersReturn(List<OrderData> getOrdersReturn)
	{
		this.getOrdersReturn = getOrdersReturn;
	}
}