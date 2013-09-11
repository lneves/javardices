package org.caudexorigo.jdbc;

import java.sql.ResultSet;

public interface ResultSetHandler<T>
{
	public T process(ResultSet rs);
}