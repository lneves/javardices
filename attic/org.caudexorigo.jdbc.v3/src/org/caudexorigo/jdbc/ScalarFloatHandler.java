package org.caudexorigo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.caudexorigo.text.StringUtils;

public class ScalarFloatHandler implements ResultSetHandler<Float>
{
	private int columnIndex;
	private String columnName;

	public ScalarFloatHandler(int columnIndex)
	{
		super();
		this.columnIndex = columnIndex;
	}

	public ScalarFloatHandler(String columnName)
	{
		super();
		this.columnName = columnName;
	}

	@Override
	public Float process(ResultSet rs)
	{
		if ((columnIndex < 1) && (StringUtils.isBlank(columnName)))
		{
			throw new IllegalStateException("A column name or a column index must be suplied");
		}

		try
		{
			if ((columnIndex >= 1) && (StringUtils.isBlank(columnName)))
			{
				return rs.getFloat(columnIndex);
			}
			else
			{
				return rs.getFloat(columnName);
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}