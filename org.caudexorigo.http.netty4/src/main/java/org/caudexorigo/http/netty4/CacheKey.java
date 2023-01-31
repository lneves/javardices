package org.caudexorigo.http.netty4;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CacheKey {
  private final Object[] keyVars;
  private TimeUnit cacheTimeUnit;
  private long cacheTime;

  public CacheKey(long cacheTime, TimeUnit cacheTimeUnit, Object... keyVars) {
    super();
    this.cacheTimeUnit = cacheTimeUnit;
    this.cacheTime = cacheTime;
    this.keyVars = keyVars;
  }

  public TimeUnit getCacheTimeUnit() {
    return cacheTimeUnit;
  }

  public void setCacheTimeUnit(TimeUnit cacheTimeUnit) {
    this.cacheTimeUnit = cacheTimeUnit;
  }

  public long getCacheTime() {
    return cacheTime;
  }

  public void setCacheTime(long cacheTime) {
    this.cacheTime = cacheTime;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(keyVars);
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
    CacheKey other = (CacheKey) obj;
    if (!Arrays.equals(keyVars, other.keyVars))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return String.format("CacheKey [keyVars=%s]", Arrays.toString(keyVars));
  }
}
