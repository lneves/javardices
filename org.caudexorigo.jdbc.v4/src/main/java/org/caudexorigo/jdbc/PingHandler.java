package org.caudexorigo.jdbc;

public interface PingHandler extends RowHandler {
  public void result(Db db, Throwable t);
}
