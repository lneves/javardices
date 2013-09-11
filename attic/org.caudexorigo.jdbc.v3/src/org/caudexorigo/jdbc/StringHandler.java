package org.caudexorigo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.caudexorigo.text.StringUtils;

public class StringHandler implements ResultSetHandler<String>
{
	private int columnIndex;
	private String columnName;

	public StringHandler(int columnIndex)
	{
		super();
		this.columnIndex = columnIndex;
	}

	public StringHandler(String columnName)
	{
		super();
		this.columnName = columnName;
	}

	@Override
	public String process(ResultSet rs)
	{
		if ((columnIndex < 1) && (StringUtils.isBlank(columnName)))
		{
			throw new IllegalStateException("A column name or a column index must be suplied");
		}

		try
		{
			if ((columnIndex >= 1) && (StringUtils.isBlank(columnName)))
			{
				return rs.getString(columnIndex);
			}
			else
			{
				return rs.getString(columnName);
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}