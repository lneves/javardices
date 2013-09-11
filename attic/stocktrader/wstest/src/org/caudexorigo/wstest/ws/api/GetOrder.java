package org.caudexorigo.wstest.ws.api;

import java.io.Serializable;

public class GetOrder implements Serializable
{
	private static final long serialVersionUID = 7196211185313275158L;

	protected int orderId;
	protected int customerId;
	protected int messageSize;

	public int getOrderId()
	{
		return this.orderId;
	}

	public void setOrderId(int orderId)
	{
		this.orderId = orderId;
	}

	public int getCustomerId()
	{
		return this.customerId;
	}

	public void setCustomerId(int customerId)
	{
		this.customerId = customerId;
	}

	public int getMessageSize()
	{
		return this.messageSize;
	}

	public void setMessageSize(int messageSize)
	{
		this.messageSize = messageSize;
	}
}