package org.caudexorigo.jpt.sample;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Customer
{
	private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM d, yyyy");

	private int id;
	private String firstName;
	private String lastName;
	private String state;
	private LocalDate birthDate;
	private String ftBirthDate;

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public LocalDate getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate)
	{
		this.birthDate = birthDate;
		ftBirthDate = birthDate.format(fmt);
	}

	public String getFtBirthDate()
	{
		return ftBirthDate;
	}
}