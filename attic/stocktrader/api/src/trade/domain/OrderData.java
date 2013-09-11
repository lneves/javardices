package trade.domain;

import java.math.BigDecimal;
import java.util.Date;

public class OrderData
{
	private int orderId;
	private int accountId;
	private int holdingId;
	private String orderType;
	private String orderStatus;
	private Date openDate;
	private Date completionDate;
	private double quantity;
	private BigDecimal price;
	private BigDecimal orderFee;
	private String symbol;

	public int getAccountId()
	{
		return accountId;
	}

	public Date getCompletionDate()
	{
		return completionDate;
	}

	public int getHoldingId()
	{
		return holdingId;
	}

	public Date getOpenDate()
	{
		return openDate;
	}

	public BigDecimal getOrderFee()
	{
		return orderFee;
	}

	public int getOrderId()
	{
		return orderId;
	}

	public String getOrderStatus()
	{
		return orderStatus;
	}

	public String getOrderType()
	{
		return orderType;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public double getQuantity()
	{
		return quantity;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setAccountId(int accountId)
	{
		this.accountId = accountId;
	}

	public void setCompletionDate(Date completionDate)
	{
		this.completionDate = completionDate;
	}

	public void setHoldingId(int holdingId)
	{
		this.holdingId = holdingId;
	}

	public void setOpenDate(Date openDate)
	{
		this.openDate = openDate;
	}

	public void setOrderFee(BigDecimal orderFee)
	{
		this.orderFee = orderFee;
	}

	public void setOrderId(int orderId)
	{
		this.orderId = orderId;
	}

	public void setOrderStatus(String orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public void setQuantity(double quantity)
	{
		this.quantity = quantity;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	@Override
	public String toString()
	{
		return String.format("OrderData [orderId=%s, accountId=%s, holdingId=%s, orderType=%s, orderStatus=%s, openDate=%s, completionDate=%s, quantity=%s, price=%s, orderFee=%s, symbol=%s]", orderId, accountId, holdingId, orderType, orderStatus, openDate, completionDate, quantity, price, orderFee, symbol);
	}
}