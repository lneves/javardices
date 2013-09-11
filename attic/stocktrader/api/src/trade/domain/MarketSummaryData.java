package trade.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MarketSummaryData
{
	private static BigDecimal ZERO = new BigDecimal(0.0);

	private BigDecimal TSIA;
	private BigDecimal openTSIA;
	private double volume;
	private List<QuoteData> topGainers;
	private List<QuoteData> topLosers;
	private Date summaryDate;

	public MarketSummaryData()
	{
		super();
		TSIA = ZERO;
		openTSIA = ZERO;
	}

	public BigDecimal getOpenTSIA()
	{
		return openTSIA;
	}

	public Date getSummaryDate()
	{
		return summaryDate;
	}

	public List<QuoteData> getTopGainers()
	{
		return topGainers;
	}

	public List<QuoteData> getTopLosers()
	{
		return topLosers;
	}

	public BigDecimal getTSIA()
	{
		return TSIA;
	}

	public double getVolume()
	{
		return volume;
	}

	public void setOpenTSIA(BigDecimal openTSIA)
	{
		if (openTSIA != null)
		{
			this.openTSIA = openTSIA;
		}
	}

	public void setSummaryDate(Date summaryDate)
	{
		this.summaryDate = summaryDate;
	}

	public void setTopGainers(List<QuoteData> topGainers)
	{
		this.topGainers = topGainers;
	}

	public void setTopLosers(List<QuoteData> topLosers)
	{
		this.topLosers = topLosers;
	}

	public void setTSIA(BigDecimal TSIA)
	{
		if (TSIA != null)
		{
			this.TSIA = TSIA;
		}
	}

	public void setVolume(double volume)
	{
		this.volume = volume;
	}

	@Override
	public String toString()
	{
		return String.format("MarketSummaryData [TSIA=%s, openTSIA=%s, volume=%s, topGainers=%s, topLosers=%s, summaryDate=%s]", TSIA, openTSIA, volume, topGainers, topLosers, summaryDate);
	}
}