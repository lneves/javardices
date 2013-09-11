package org.caudexorigo.wstest.domain;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable
{
	private static final long serialVersionUID = -1552635344249240516L;

	private int customerId;
	private String contactFirstName;
	private String contactLastName;
	private String contactPhone;
	private Date lastActivityDate;
	private String creditCardNumber;
	private String creditCardExpirationDate;
	private Address billingAddress;
	private Address shippingAddress;

	public int getCustomerId()
	{
		return this.customerId;
	}

	public void setCustomerId(int customerId)
	{
		this.customerId = customerId;
	}

	public String getContactFirstName()
	{
		return this.contactFirstName;
	}

	public void setContactFirstName(String contactFirstName)
	{
		this.contactFirstName = contactFirstName;
	}

	public String getContactLastName()
	{
		return this.contactLastName;
	}

	public void setContactLastName(String contactLastName)
	{
		this.contactLastName = contactLastName;
	}

	public String getContactPhone()
	{
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone)
	{
		this.contactPhone = contactPhone;
	}

	public Date getLastActivityDate()
	{
		return this.lastActivityDate;
	}

	public void setLastActivityDate(Date lastActivityDate)
	{
		this.lastActivityDate = lastActivityDate;
	}

	public String getCreditCardNumber()
	{
		return this.creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber)
	{
		this.creditCardNumber = creditCardNumber;
	}

	public String getCreditCardExpirationDate()
	{
		return this.creditCardExpirationDate;
	}

	public void setCreditCardExpirationDate(String creditCardExpirationDate)
	{
		this.creditCardExpirationDate = creditCardExpirationDate;
	}

	public Address getBillingAddress()
	{
		return this.billingAddress;
	}

	public void setBillingAddress(Address billingAddress)
	{
		this.billingAddress = billingAddress;
	}

	public Address getShippingAddress()
	{
		return this.shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress)
	{
		this.shippingAddress = shippingAddress;
	}

	@Override
	public String toString()
	{
		return String.format("Customer [customerId=%s, contactFirstName=%s, contactLastName=%s, contactPhone=%s, lastActivityDate=%s, creditCardNumber=%s, creditCardExpirationDate=%s, billingAddress=%s, shippingAddress=%s]", customerId, contactFirstName, contactLastName, contactPhone, lastActivityDate, creditCardNumber, creditCardExpirationDate, billingAddress, shippingAddress);
	}
}