package org.caudexorigo.jdbc;

import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScalarConverter<T> implements RowConverter<T> {
  private int columnIndex;
  private String columnName;
  private final Class<T> clazz;

  public ScalarConverter(int columnIndex, Class<T> clazz) {
    super();
    this.columnIndex = columnIndex;
    this.clazz = clazz;
  }

  public ScalarConverter(String columnName, Class<T> clazz) {
    super();
    this.columnName = columnName;
    this.clazz = clazz;
  }

  @Override
  public T process(ResultSet rs) {
    if ((columnIndex < 1) && (StringUtils.isBlank(columnName))) {
      throw new IllegalStateException("A column name or a column index must be suplied");
    }

    try {
      if ((columnIndex >= 1) && (StringUtils.isBlank(columnName))) {
        return getValue(rs, columnIndex);
      } else {
        return getValue(rs, columnName);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private T getValue(ResultSet rs, int ix) throws SQLException {
    return clazz.cast(rs.getObject(ix));
  }

  private T getValue(ResultSet rs, String field) throws SQLException {
    return clazz.cast(rs.getObject(field));
  }
}
