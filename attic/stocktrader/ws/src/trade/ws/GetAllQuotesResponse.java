package trade.ws;

import java.util.List;

import trade.domain.QuoteData;

public class GetAllQuotesResponse
{
	private List<QuoteData> getAllQuotesReturn;

	public List<QuoteData> getAllQuotesReturn()
	{
		return getAllQuotesReturn;
	}

	public void setAllQuotesReturn(List<QuoteData> getAllQuotesReturn)
	{
		this.getAllQuotesReturn = getAllQuotesReturn;
	}
}
