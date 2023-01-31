package org.caudexorigo.ds;

public interface CacheFiller<K, V> {
  public V populate(K key);
}
