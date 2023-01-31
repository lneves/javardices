package org.caudexorigo.jdbc;

import java.sql.CallableStatement;

class CallableStatementEntry {
  private final long expires;

  private final CallableStatement entry;

  private String cacheKey;

  public CallableStatementEntry(int ttl, CallableStatement entry, String cacheKey) {
    super();
    this.cacheKey = cacheKey;
    this.expires = System.currentTimeMillis() + (ttl * 1000);
    this.entry = entry;
  }

  protected final boolean isStale() {
    return System.currentTimeMillis() > expires;
  }

  protected final CallableStatement get() {
    return entry;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cacheKey == null) ? 0 : cacheKey.hashCode());
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
    CallableStatementEntry other = (CallableStatementEntry) obj;
    if (cacheKey == null) {
      if (other.cacheKey != null)
        return false;
    } else if (!cacheKey.equals(other.cacheKey))
      return false;
    return true;
  }
}
