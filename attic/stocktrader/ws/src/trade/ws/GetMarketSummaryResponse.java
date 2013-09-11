package trade.ws;

import trade.domain.MarketSummaryData;

public class GetMarketSummaryResponse
{
	private MarketSummaryData getMarketSummaryReturn;

	public MarketSummaryData getMarketSummaryReturn()
	{
		return getMarketSummaryReturn;
	}

	public void setMarketSummaryReturn(MarketSummaryData getMarketSummaryReturn)
	{
		this.getMarketSummaryReturn = getMarketSummaryReturn;
	}
}
