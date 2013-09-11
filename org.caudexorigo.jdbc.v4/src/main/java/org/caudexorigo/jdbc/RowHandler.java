package org.caudexorigo.jdbc;

import java.sql.ResultSet;

public interface RowHandler
{
	public void process(ResultSet rs);
}