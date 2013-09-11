package trade.ws;

public class Buy
{
	private String userID;
	private String symbol;
	private Double quantity;
	private int orderProcessingMode;

	public String getUserId()
	{
		return userID;
	}

	public void setUserId(String userID)
	{
		this.userID = userID;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public Double getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Double quantity)
	{
		this.quantity = quantity;
	}

	public int getOrderProcessingMode()
	{
		return orderProcessingMode;
	}

	public void setOrderProcessingMode(int orderProcessingMode)
	{
		this.orderProcessingMode = orderProcessingMode;
	}
}
