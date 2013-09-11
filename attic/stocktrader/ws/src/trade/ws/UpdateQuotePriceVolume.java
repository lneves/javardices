package trade.ws;

import java.math.BigDecimal;

public class UpdateQuotePriceVolume
{
	private String symbol;
	private BigDecimal newPrice;
	private Double sharesTraded;

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public BigDecimal getNewPrice()
	{
		return newPrice;
	}

	public void setNewPrice(BigDecimal newPrice)
	{
		this.newPrice = newPrice;
	}

	public Double getSharesTraded()
	{
		return sharesTraded;
	}

	public void setSharesTraded(Double sharesTraded)
	{
		this.sharesTraded = sharesTraded;
	}
}
