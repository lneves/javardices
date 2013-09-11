package org.caudexorigo.wstest.domain;

import java.io.Serializable;

public class TestStruct implements Serializable
{
	private static final long serialVersionUID = -6467413219779176755L;

	protected int i;
	protected float f;
	protected String s;

	public int getI()
	{
		return this.i;
	}

	public void setI(int i)
	{
		this.i = i;
	}

	public float getF()
	{
		return this.f;
	}

	public void setF(float f)
	{
		this.f = f;
	}

	public String getS()
	{
		return this.s;
	}

	public void setS(String s)
	{
		this.s = s;
	}

	@Override
	public String toString()
	{
		return String.format("TestStruct [i=%s, f=%s, s=%s]", i, f, s);
	}
}