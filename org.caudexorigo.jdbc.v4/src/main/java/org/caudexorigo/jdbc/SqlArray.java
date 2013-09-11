package org.caudexorigo.jdbc;

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
}