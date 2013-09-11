package org.caudexorigo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.caudexorigo.text.StringUtils;

public class ScalarIntegerHandler implements ResultSetHandler<Integer>
{
	private int columnIndex;
	private String columnName;

	public ScalarIntegerHandler(int columnIndex)
	{
		super();
		this.columnIndex = columnIndex;
	}

	public ScalarIntegerHandler(String columnName)
	{
		super();
		this.columnName = columnName;
	}

	@Override
	public Integer process(ResultSet rs)
	{
		if ((columnIndex < 1) && (StringUtils.isBlank(columnName)))
		{
			throw new IllegalStateException("A column name or a column index must be suplied");
		}

		try
		{
			if ((columnIndex >= 1) && (StringUtils.isBlank(columnName)))
			{
				return rs.getInt(columnIndex);
			}
			else
			{
				return rs.getInt(columnName);
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}