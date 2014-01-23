package org.caudexorigo.jdbc;

import java.sql.ResultSet;

public interface RowHandler
{
	public void beforeFirst(ResultSet rs);

	public void process(ResultSet rs);

	public void afterLast(ResultSet rs);
}