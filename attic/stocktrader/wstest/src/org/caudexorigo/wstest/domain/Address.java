package org.caudexorigo.wstest.domain;

import java.io.Serializable;

public class Address implements Serializable
{
	private static final long serialVersionUID = 8413666803355914812L;

	protected String firstName;
	protected String lastName;
	protected String address1;
	protected String address2;
	protected String city;
	protected String state;
	protected String zip;

	public String getFirstName()
	{
		return this.firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getAddress1()
	{
		return this.address1;
	}

	public void setAddress1(String address1)
	{
		this.address1 = address1;
	}

	public String getAddress2()
	{
		return this.address2;
	}

	public void setAddress2(String address2)
	{
		this.address2 = address2;
	}

	public String getCity()
	{
		return this.city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return this.state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getZip()
	{
		return this.zip;
	}

	public void setZip(String zip)
	{
		this.zip = zip;
	}

	@Override
	public String toString()
	{
		return String.format("Address [firstName=%s, lastName=%s, address1=%s, address2=%s, city=%s, state=%s, zip=%s]", firstName, lastName, address1, address2, city, state, zip);
	}
}