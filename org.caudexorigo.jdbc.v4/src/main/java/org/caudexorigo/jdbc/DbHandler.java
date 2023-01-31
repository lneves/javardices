package org.caudexorigo.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbHandler {
  public DbHandler() {
    super();
  }

  public void handle(final DbPool dbPool, final String sql, final RowHandler row_handler,
      Object... params) {
    Db db = null;

    try {
      db = dbPool.pick();
      handle(db, sql, row_handler, params);
    } catch (Throwable ex) {
      throw new RuntimeException(ex);
    } finally {
      dbPool.release(db);
    }
  }

  public void handle(final Db db, final String sql, final RowHandler row_handler,
      final Object... params) {
    PreparedStatement ps = null;

    try {
      ps = db.getPreparedStatement(sql);
      handle(db, ps, row_handler, params);
    } catch (Throwable ex) {
      throw new RuntimeException(ex);
    } finally {
      if (!db.useStatementCache()) {
        Db.closeQuietly(ps);
      }
    }
  }

  public void handle(final Db db, final PreparedStatement pstmt, final RowHandler row_handler,
      final Object... params) {
    ResultSet rs = null;

    try {
      rs = db.fetchResultSetWithPreparedStatment(pstmt, params);

      row_handler.beforeFirst(rs);

      while (rs.next()) {
        row_handler.process(rs);
      }

      row_handler.afterLast(rs);
    } catch (Throwable ex) {
      throw new RuntimeException(ex);
    } finally {
      Db.closeQuietly(rs);
    }
  }
}
