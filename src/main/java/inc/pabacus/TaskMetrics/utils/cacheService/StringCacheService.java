package inc.pabacus.TaskMetrics.utils.cacheService;

import java.util.HashMap;
import java.util.Map;

public class StringCacheService implements CacheService<CacheKey, String> {
  private static final Map<CacheKey, String> cache = new HashMap<>();

  @Override
  public String put(CacheKey key, String value) {
    return cache.put(key, value);
  }

  @Override
  public String get(CacheKey key) {
    return cache.get(key);
  }

  @Override
  public void remove(CacheKey key) {
    cache.remove(key);
  }

  @Override
  public void clear() {
    cache.clear();
  }
}
