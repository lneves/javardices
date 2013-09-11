package trade.ws;

import trade.domain.HoldingData;

public class GetHoldingResponse
{
	private HoldingData getHoldingReturn;

	public HoldingData getHoldingReturn()
	{
		return getHoldingReturn;
	}

	public void setHoldingReturn(HoldingData getHoldingReturn)
	{
		this.getHoldingReturn = getHoldingReturn;
	}
}
