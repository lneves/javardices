package org.caudexorigo.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.caudexorigo.text.StringUtils;

public class ScalarBigDecimalHandler implements ResultSetHandler<BigDecimal>
{
	private int columnIndex;
	private String columnName;

	public ScalarBigDecimalHandler(int columnIndex)
	{
		super();
		this.columnIndex = columnIndex;
	}

	public ScalarBigDecimalHandler(String columnName)
	{
		super();
		this.columnName = columnName;
	}

	@Override
	public BigDecimal process(ResultSet rs)
	{
		if ((columnIndex < 1) && (StringUtils.isBlank(columnName)))
		{
			throw new IllegalStateException("A column name or a column index must be suplied");
		}

		try
		{
			if ((columnIndex >= 1) && (StringUtils.isBlank(columnName)))
			{
				return rs.getBigDecimal(columnIndex);
			}
			else
			{
				return rs.getBigDecimal(columnName);
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}