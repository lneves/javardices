package trade.ws;

import trade.domain.RunStatsData;

public class ResetTradeResponse
{
	private RunStatsData resetTradeReturn;

	public RunStatsData getResetTradeReturn()
	{
		return resetTradeReturn;
	}

	public void setResetTradeReturn(RunStatsData resetTradeReturn)
	{
		this.resetTradeReturn = resetTradeReturn;
	}
}