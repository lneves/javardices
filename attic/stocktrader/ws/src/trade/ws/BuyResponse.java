package trade.ws;

import trade.domain.OrderData;

public class BuyResponse
{
	private OrderData buyReturn;

	public OrderData getBuyReturn()
	{
		return buyReturn;
	}

	public void setBuyReturn(OrderData buyReturn)
	{
		this.buyReturn = buyReturn;
	}
}