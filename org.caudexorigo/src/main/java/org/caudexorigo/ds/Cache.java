package org.caudexorigo.ds;

import org.caudexorigo.ErrorAnalyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class Cache<K, V> {
  // private static Logger log = LoggerFactory.getLogger(Cache.class);

  private final ConcurrentMap<K, FutureTask<V>> map = new ConcurrentHashMap<K, FutureTask<V>>();

  public V get(final K key, final CacheFiller<K, V> cf) throws InterruptedException {
    FutureTask<V> f = map.get(key);
    if (f == null) {
      Callable<V> c = new Callable<V>() {
        public V call() throws InterruptedException {
          // return value associated with key
          return cf.populate(key);
        }
      };
      f = new FutureTask<V>(c);
      FutureTask<V> old = map.putIfAbsent(key, f);
      if (old == null) {
        try {
          // log.debug("Cache miss. Populating cache, key: {}", key.toString());
          f.run();
        } catch (Throwable t) {
          map.remove(key);
          throw new RuntimeException(t);
        }
      } else {
        // log.debug("Cache hit. Retrieve from cache, key: {}", key.toString());
        f = old;
      }
    }

    try {
      return f.get();
    } catch (Throwable t) {
      map.remove(key);
      throw new RuntimeException(t);
    }
  }

  public boolean containsKey(K key) throws InterruptedException {
    return map.containsKey(key);
  }

  public void remove(K key) throws InterruptedException {
    map.remove(key);
  }

  public void removeValue(V value) throws InterruptedException {
    try {
      Set<Entry<K, FutureTask<V>>> items = map.entrySet();

      for (Entry<K, FutureTask<V>> entry : items) {
        if (entry.getValue().get().equals(value)) {
          items.remove(entry);
        }
      }
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  public Collection<V> values() throws InterruptedException {
    Collection<FutureTask<V>> fvalues = map.values();
    Collection<V> values = new ArrayList<V>(fvalues.size());

    for (FutureTask<V> fv : fvalues) {
      try {
        values.add(fv.get());
      } catch (Throwable t) {
        Throwable r = ErrorAnalyser.findRootCause(t);
        System.out.printf("Cache.values() -> %s%n", r.getMessage());
      }
    }

    return values;
  }

  public Set<K> keys() {
    return map.keySet();
  }

  public int size() {
    return map.size();
  }
}
