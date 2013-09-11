package org.caudexorigo.wstest.domain;

import java.util.Arrays;

public class Synthetic
{
	protected String strng;
	protected TestStruct strct;
	protected byte[] barray;

	public String getStrng()
	{
		return this.strng;
	}

	public void setStrng(String strng)
	{
		this.strng = strng;
	}

	public TestStruct getStrct()
	{
		return this.strct;
	}

	public void setStrct(TestStruct strct)
	{
		this.strct = strct;
	}

	public byte[] getBarray()
	{
		return this.barray;
	}

	public void setBarray(byte[] barray)
	{
		this.barray = barray;
	}

	@Override
	public String toString()
	{
		return String.format("Synthetic [strng=%s, strct=%s, barray=%s]", strng, strct, Arrays.toString(barray));
	}
}