package org.caudexorigo.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public class DbRunner {
  public DbRunner() {
    super();
  }

  public int executeCallableStatement(Db db, String proc) {
    return executeCallableStatement(db, proc, new Object[0]);
  }

  public int executeCallableStatement(Db db, String proc, Object... params) {
    return db.executeCallableStatement(proc, params);
  }

  public int executeCallableStatement(Db db, CallableStatement cs, Object... params) {
    return db.executeCallableStatement(cs, params);
  }

  public int executeCallableStatement(final DbPool dbPool, String proc) {
    return executeCallableStatement(dbPool, proc, new Object[0]);
  }

  public int executeCallableStatement(final DbPool dbPool, String proc, Object... params) {
    Db db = null;

    try {
      db = dbPool.obtain();
      return executeCallableStatement(db, proc, params);
    } catch (Throwable ex) {
      throw new RuntimeException(ex);
    } finally {
      dbPool.release(db);
    }
  }

  public int executePreparedStatement(Db db, String sql, Object... params) {
    if ((params != null) && (params.length > 0)) {
      return db.executePreparedStatement(sql, params);
    } else {
      return db.executeStatement(sql);
    }
  }

  public int executePreparedStatement(Db db, PreparedStatement pstmt, Object... params) {
    return db.executePreparedStatement(pstmt, params);
  }

  public int executePreparedStatement(final DbPool dbPool, String sql, Object... params) {
    Db db = null;

    try {
      db = dbPool.obtain();
      return executePreparedStatement(db, sql, params);
    } catch (Throwable ex) {
      throw new RuntimeException(ex);
    } finally {
      dbPool.release(db);
    }
  }

  public int executeStatement(Db db, String sql) {
    return executePreparedStatement(db, sql, new Object[0]);
  }

  public int executeStatement(final DbPool dbPool, String sql) {
    return executePreparedStatement(dbPool, sql, new Object[0]);
  }
}
