package trade.ws;

public class OrderCompleted
{
	private String userID;
	private Integer orderID;

	public String getUserId()
	{
		return userID;
	}

	public void setUserId(String userID)
	{
		this.userID = userID;
	}

	public Integer getOrderId()
	{
		return orderID;
	}

	public void setOrderId(Integer orderID)
	{
		this.orderID = orderID;
	}
}
