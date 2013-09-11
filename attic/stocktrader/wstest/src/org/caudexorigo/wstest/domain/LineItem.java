package org.caudexorigo.wstest.domain;

import java.io.Serializable;

public class LineItem implements Serializable
{
	private static final long serialVersionUID = 6652070364900749276L;

	protected int orderId;
	protected int itemId;
	protected int productId;
	protected String productDescription;
	protected int orderQuantity;
	protected float unitPrice;

	public int getOrderId()
	{
		return this.orderId;
	}

	public void setOrderId(int orderId)
	{
		this.orderId = orderId;
	}

	public int getItemId()
	{
		return this.itemId;
	}

	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}

	public int getProductId()
	{
		return this.productId;
	}

	public void setProductId(int productId)
	{
		this.productId = productId;
	}

	public String getProductDescription()
	{
		return this.productDescription;
	}

	public void setProductDescription(String productDescription)
	{
		this.productDescription = productDescription;
	}

	public int getOrderQuantity()
	{
		return this.orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity)
	{
		this.orderQuantity = orderQuantity;
	}

	public float getUnitPrice()
	{
		return this.unitPrice;
	}

	public void setUnitPrice(float unitPrice)
	{
		this.unitPrice = unitPrice;
	}

	@Override
	public String toString()
	{
		return String.format("LineItem [orderId=%s, itemId=%s, productId=%s, productDescription=%s, orderQuantity=%s, unitPrice=%s]", orderId, itemId, productId, productDescription, orderQuantity, unitPrice);
	}
}