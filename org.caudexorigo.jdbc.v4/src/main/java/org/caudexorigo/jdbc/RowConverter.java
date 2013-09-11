package org.caudexorigo.jdbc;

import java.sql.ResultSet;

public interface RowConverter<T>
{
	public T process(ResultSet rs);
}