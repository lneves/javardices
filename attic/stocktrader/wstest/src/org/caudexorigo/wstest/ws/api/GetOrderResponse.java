package org.caudexorigo.wstest.ws.api;

import org.caudexorigo.wstest.domain.Order;

public class GetOrderResponse
{
	protected Order getOrderResult;

	public Order getGetOrderResult()
	{
		return this.getOrderResult;
	}

	public void setGetOrderResult(Order getOrderResult)
	{
		this.getOrderResult = getOrderResult;
	}
}