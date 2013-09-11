package org.caudexorigo.wstest.domain;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable
{
	private static final long serialVersionUID = 2086402924663761379L;

	private int orderId;
	private int orderStatus;
	private Date orderDate;
	private float orderTotalAmount;
	private Customer customer;
	private COfLineItem cOfLineItem;

	public int getOrderId()
	{
		return this.orderId;
	}

	public void setOrderId(int orderId)
	{
		this.orderId = orderId;
	}

	public int getOrderStatus()
	{
		return this.orderStatus;
	}

	public void setOrderStatus(int orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public Date getOrderDate()
	{
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}

	public float getOrderTotalAmount()
	{
		return this.orderTotalAmount;
	}

	public void setOrderTotalAmount(float orderTotalAmount)
	{
		this.orderTotalAmount = orderTotalAmount;
	}

	public Customer getCustomer()
	{
		return this.customer;
	}

	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

	public COfLineItem getCOfLineItem()
	{
		return this.cOfLineItem;
	}

	public void setCOfLineItem(COfLineItem lineItems)
	{
		this.cOfLineItem = lineItems;
	}

	@Override
	public String toString()
	{
		return String.format("Order [orderId=%s, orderStatus=%s, orderDate=%s, orderTotalAmount=%s, customer=%s, cOfLineItem=%s]", orderId, orderStatus, orderDate, orderTotalAmount, customer, cOfLineItem);
	}
}