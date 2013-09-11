package trade.ws;

import trade.domain.QuoteData;

public class CreateQuoteResponse
{
	private QuoteData createQuoteReturn;

	public QuoteData getCreateQuoteReturn()
	{
		return createQuoteReturn;
	}

	public void setCreateQuoteReturn(QuoteData createQuoteReturn)
	{
		this.createQuoteReturn = createQuoteReturn;
	}
}
