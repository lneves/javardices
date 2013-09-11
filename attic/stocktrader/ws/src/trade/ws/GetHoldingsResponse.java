package trade.ws;

import java.util.List;

import trade.domain.HoldingData;

public class GetHoldingsResponse
{
	private List<HoldingData> getHoldingsReturn;

	public List<HoldingData> getHoldingsReturn()
	{
		return getHoldingsReturn;
	}

	public void setHoldingsReturn(List<HoldingData> getHoldingsReturn)
	{
		this.getHoldingsReturn = getHoldingsReturn;
	}
}