package org.caudexorigo.wstest.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class COfTestStruct implements Serializable
{
	private static final long serialVersionUID = -32931697681956572L;

	private List<TestStruct> tsList;

	public void addTestStruct(TestStruct ts)
	{
		tsList.add(ts);
	}

	public TestStruct getTestStructByIndex(int index)
	{
		return (TestStruct) tsList.get(index);
	}

	public final List<TestStruct> getTsList()
	{
		return tsList;
	}

	public int sizeOfCollection()
	{
		return tsList.size();
	}

	protected final void setTsList(List<TestStruct> tsList)
	{
		this.tsList = tsList;
	}

	public COfTestStruct()
	{
		super();
		tsList = new ArrayList<TestStruct>();
	}
}