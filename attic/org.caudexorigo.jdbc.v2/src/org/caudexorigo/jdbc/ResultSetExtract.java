package org.caudexorigo.jdbc;

import java.sql.ResultSet;

public interface ResultSetExtract<T>
{
	public T process(ResultSet rs);

}
