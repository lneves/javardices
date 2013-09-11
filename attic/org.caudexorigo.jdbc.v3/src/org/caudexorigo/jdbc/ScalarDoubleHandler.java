package org.caudexorigo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.caudexorigo.text.StringUtils;

public class ScalarDoubleHandler implements ResultSetHandler<Double>
{
	private int columnIndex;
	private String columnName;

	public ScalarDoubleHandler(int columnIndex)
	{
		super();
		this.columnIndex = columnIndex;
	}

	public ScalarDoubleHandler(String columnName)
	{
		super();
		this.columnName = columnName;
	}

	@Override
	public Double process(ResultSet rs)
	{
		if ((columnIndex < 1) && (StringUtils.isBlank(columnName)))
		{
			throw new IllegalStateException("A column name or a column index must be suplied");
		}

		try
		{
			if ((columnIndex >= 1) && (StringUtils.isBlank(columnName)))
			{
				return rs.getDouble(columnIndex);
			}
			else
			{
				return rs.getDouble(columnName);
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}