package org.caudexorigo.jdbc;

/**
 * Class representing a null value in a prepared statement. Used with
 * PreparedStatementFactory.
 * 
 */
class NullSQLType {

  /**
   * The expected sql type of the field.
   */
  private int type;

  /**
   * Default constructor. Takes the expected sql type of the field that will be null.
   * 
   * @param fieldType the expected field type in the db. Should be pulled from
   *        java.sql.Types.
   * @see java.sql.Types
   */
  public NullSQLType(int fieldType) {
    type = fieldType;
  }

  /**
   * Get the sql type of the field.
   * 
   * @return the sql type of the field
   */
  public int getFieldType() {
    return type;
  }

  /**
   * Get string representation of this object.
   * 
   * @return string representation of this object
   */
  public String toString() {
    return "Null sql type:" + type;
  }
}
