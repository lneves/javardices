package org.caudexorigo.jdbc;

import java.sql.ResultSet;

public abstract class DefaultRowHandler implements RowHandler
{
	@Override
	public void beforeFirst(ResultSet rs)
	{
		// do nothing
	}

	@Override
	public void afterLast(ResultSet rs)
	{
		// do nothing
	}
}