package org.caudexorigo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.caudexorigo.text.StringUtils;

public class ScalarLongConverter implements RowConverter<Long>
{
	private int columnIndex;
	private String columnName;

	public ScalarLongConverter(int columnIndex)
	{
		super();
		this.columnIndex = columnIndex;
	}

	public ScalarLongConverter(String columnName)
	{
		super();
		this.columnName = columnName;
	}

	@Override
	public Long process(ResultSet rs)
	{
		if ((columnIndex < 1) && (StringUtils.isBlank(columnName)))
		{
			throw new IllegalStateException("A column name or a column index must be suplied");
		}

		try
		{
			if ((columnIndex >= 1) && (StringUtils.isBlank(columnName)))
			{
				return rs.getLong(columnIndex);
			}
			else
			{
				return rs.getLong(columnName);
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}