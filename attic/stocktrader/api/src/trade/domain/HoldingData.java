package trade.domain;

import java.math.BigDecimal;
import java.util.Date;

public class HoldingData
{
	private int holdingId;
	private double quantity;
	private BigDecimal purchasePrice;
	private Date purchaseDate;
	private String quoteId;

	public int getHoldingId()
	{
		return holdingId;
	}

	public Date getPurchaseDate()
	{
		return purchaseDate;
	}

	public BigDecimal getPurchasePrice()
	{
		return purchasePrice;
	}

	public double getQuantity()
	{
		return quantity;
	}

	public String getQuoteId()
	{
		return quoteId;
	}

	public void setHoldingId(int holdingId)
	{
		this.holdingId = holdingId;
	}

	public void setPurchaseDate(Date purchaseDate)
	{
		this.purchaseDate = purchaseDate;
	}

	public void setPurchasePrice(BigDecimal purchasePrice)
	{
		this.purchasePrice = purchasePrice;
	}

	public void setQuantity(double quantity)
	{
		this.quantity = quantity;
	}

	public void setQuoteId(String quoteId)
	{
		this.quoteId = quoteId;
	}

	@Override
	public String toString()
	{
		return String.format("HoldingData [holdingId=%s, quantity=%s, purchasePrice=%s, purchaseDate=%s, quoteId=%s]", holdingId, quantity, purchasePrice, purchaseDate, quoteId);
	}
}