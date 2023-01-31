package org.caudexorigo.jpt;

public interface CacheFiller<K, V> {
  public V populate(K key);
}
