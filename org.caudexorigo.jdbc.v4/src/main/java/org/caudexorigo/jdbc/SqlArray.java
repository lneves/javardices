package org.caudexorigo.jdbc;

import java.util.Arrays;

public class SqlArray
{
	public final String typeName;
	public final Object[] elements;

	public SqlArray(String typeName, Object[] elements)
	{
		super();
		this.typeName = typeName;
		this.elements = elements;
	}

	@Override
	public String toString()
	{
		return String.format("SqlArray [typeName=%s, elements=%s]", typeName, Arrays.toString(elements));
	}
}