package inc.pabacus.TaskMetrics.utils.cacheService;

public interface CacheService<K, V> {
  V put(K key, V value);

  V get(K key);

  void remove(K key);

  void clear();
}
