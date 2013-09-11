package trade.ws;

import trade.domain.QuoteData;

public class UpdateQuotePriceVolumeResponse
{
	private QuoteData updateQuotePriceVolumeReturn;

	public QuoteData getUpdateQuotePriceVolumeReturn()
	{
		return updateQuotePriceVolumeReturn;
	}

	public void setUpdateQuotePriceVolumeReturn(QuoteData updateQuotePriceVolumeReturn)
	{
		this.updateQuotePriceVolumeReturn = updateQuotePriceVolumeReturn;
	}
}
