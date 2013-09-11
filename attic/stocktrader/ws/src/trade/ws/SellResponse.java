package trade.ws;

import trade.domain.OrderData;

public class SellResponse
{
	private OrderData sellReturn;

	public OrderData getSellReturn()
	{
		return sellReturn;
	}

	public void setSellReturn(OrderData sellReturn)
	{
		this.sellReturn = sellReturn;
	}
}
