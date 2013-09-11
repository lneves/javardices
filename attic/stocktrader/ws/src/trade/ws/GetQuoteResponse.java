package trade.ws;

import trade.domain.QuoteData;

public class GetQuoteResponse
{
	private QuoteData getQuoteReturn;

	public QuoteData getQuoteReturn()
	{
		return getQuoteReturn;
	}

	public void setQuoteReturn(QuoteData getQuoteReturn)
	{
		this.getQuoteReturn = getQuoteReturn;
	}
}
