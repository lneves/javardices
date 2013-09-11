package trade.ws;

public class Sell
{
	private String userID;
	private Integer holdingID;
	private int orderProcessingMode;

	public String getUserId()
	{
		return userID;
	}

	public void setUserId(String userID)
	{
		this.userID = userID;
	}

	public Integer getHoldingId()
	{
		return holdingID;
	}

	public void setHoldingId(Integer holdingID)
	{
		this.holdingID = holdingID;
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
