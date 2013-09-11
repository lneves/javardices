package trade.ws;

public class CompleteOrder
{
	private Integer orderID;
	private boolean twoPhase;

	public Integer getOrderId()
	{
		return orderID;
	}

	public void setOrderId(Integer orderID)
	{
		this.orderID = orderID;
	}

	public boolean isTwoPhase()
	{
		return twoPhase;
	}

	public void setTwoPhase(boolean twoPhase)
	{
		this.twoPhase = twoPhase;
	}
}
