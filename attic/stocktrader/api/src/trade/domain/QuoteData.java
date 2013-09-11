package trade.domain;

import java.math.BigDecimal;

public class QuoteData
{
	private String symbol;
	private String companyName;
	private BigDecimal price;
	private BigDecimal open;
	private BigDecimal low;
	private BigDecimal high;
	private double change;
	private double volume;

	public double getChange()
	{
		return change;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public BigDecimal getHigh()
	{
		return high;
	}

	public BigDecimal getLow()
	{
		return low;
	}

	public BigDecimal getOpen()
	{
		return open;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public double getVolume()
	{
		return volume;
	}

	public void setChange(double change)
	{
		this.change = change;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public void setHigh(BigDecimal high)
	{
		this.high = high;
	}

	public void setLow(BigDecimal low)
	{
		this.low = low;
	}

	public void setOpen(BigDecimal open)
	{
		this.open = open;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public void setVolume(double volume)
	{
		this.volume = volume;
	}

	@Override
	public String toString()
	{
		return String.format("QuoteData [symbol=%s, companyName=%s, price=%s, open=%s, low=%s, high=%s, change=%s, volume=%s]", symbol, companyName, price, open, low, high, change, volume);
	}
}