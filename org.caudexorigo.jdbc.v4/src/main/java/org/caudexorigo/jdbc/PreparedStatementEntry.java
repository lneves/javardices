package org.caudexorigo.jdbc;

import java.sql.PreparedStatement;

class PreparedStatementEntry {
  private final long expires;

  private final PreparedStatement entry;

  private final String sql;

  public PreparedStatementEntry(int ttl, PreparedStatement entry, String sql) {
    super();
    this.expires = System.currentTimeMillis() + (ttl * 1000);
    this.entry = entry;
    this.sql = sql;
  }

  protected final boolean isStale() {
    return System.currentTimeMillis() > expires;
  }

  protected final PreparedStatement get() {
    return entry;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sql == null) ? 0 : sql.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PreparedStatementEntry other = (PreparedStatementEntry) obj;
    if (sql == null) {
      if (other.sql != null)
        return false;
    } else if (!sql.equals(other.sql))
      return false;
    return true;
  }
}
