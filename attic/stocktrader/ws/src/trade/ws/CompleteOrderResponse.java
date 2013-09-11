package trade.ws;

import trade.domain.OrderData;

public class CompleteOrderResponse
{
	private OrderData completeOrderReturn;

	public OrderData getCompleteOrderReturn()
	{
		return completeOrderReturn;
	}

	public void setCompleteOrderReturn(OrderData completeOrderReturn)
	{
		this.completeOrderReturn = completeOrderReturn;
	}
}
