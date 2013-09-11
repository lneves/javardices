package trade.domain;

public class RunStatsData
{
	private int tradeUserCount;
	private int newUserCount;
	private int sumLoginCount;
	private int sumLogoutCount;
	private int holdingCount;
	private int buyOrderCount;
	private int sellOrderCount;
	private int cancelledOrderCount;
	private int openOrderCount;
	private int deletedOrderCount;
	private int orderCount;
	private int tradeStockCount;

	public int getTradeUserCount()
	{
		return tradeUserCount;
	}

	public void setTradeUserCount(int tradeUserCount)
	{
		this.tradeUserCount = tradeUserCount;
	}

	public int getNewUserCount()
	{
		return newUserCount;
	}

	public void setNewUserCount(int newUserCount)
	{
		this.newUserCount = newUserCount;
	}

	public int getSumLoginCount()
	{
		return sumLoginCount;
	}

	public void setSumLoginCount(int sumLoginCount)
	{
		this.sumLoginCount = sumLoginCount;
	}

	public int getSumLogoutCount()
	{
		return sumLogoutCount;
	}

	public void setSumLogoutCount(int sumLogoutCount)
	{
		this.sumLogoutCount = sumLogoutCount;
	}

	public int getHoldingCount()
	{
		return holdingCount;
	}

	public void setHoldingCount(int holdingCount)
	{
		this.holdingCount = holdingCount;
	}

	public int getBuyOrderCount()
	{
		return buyOrderCount;
	}

	public void setBuyOrderCount(int buyOrderCount)
	{
		this.buyOrderCount = buyOrderCount;
	}

	public int getSellOrderCount()
	{
		return sellOrderCount;
	}

	public void setSellOrderCount(int sellOrderCount)
	{
		this.sellOrderCount = sellOrderCount;
	}

	public int getCancelledOrderCount()
	{
		return cancelledOrderCount;
	}

	public void setCancelledOrderCount(int cancelledOrderCount)
	{
		this.cancelledOrderCount = cancelledOrderCount;
	}

	public int getOpenOrderCount()
	{
		return openOrderCount;
	}

	public void setOpenOrderCount(int openOrderCount)
	{
		this.openOrderCount = openOrderCount;
	}

	public int getDeletedOrderCount()
	{
		return deletedOrderCount;
	}

	public void setDeletedOrderCount(int deletedOrderCount)
	{
		this.deletedOrderCount = deletedOrderCount;
	}

	public int getOrderCount()
	{
		return orderCount;
	}

	public void setOrderCount(int orderCount)
	{
		this.orderCount = orderCount;
	}

	public int getTradeStockCount()
	{
		return tradeStockCount;
	}

	public void setTradeStockCount(int tradeStockCount)
	{
		this.tradeStockCount = tradeStockCount;
	}

	@Override
	public String toString()
	{
		return String.format("RunStatsData [tradeUserCount=%s, newUserCount=%s, sumLoginCount=%s, sumLogoutCount=%s, holdingCount=%s, buyOrderCount=%s, sellOrderCount=%s, cancelledOrderCount=%s, openOrderCount=%s, deletedOrderCount=%s, orderCount=%s, tradeStockCount=%s]", tradeUserCount, newUserCount, sumLoginCount, sumLogoutCount, holdingCount, buyOrderCount, sellOrderCount, cancelledOrderCount, openOrderCount, deletedOrderCount, orderCount, tradeStockCount);
	}	
}