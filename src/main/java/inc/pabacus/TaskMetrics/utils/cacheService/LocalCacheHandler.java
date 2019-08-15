package inc.pabacus.TaskMetrics.utils.cacheService;

/**
 * Additional service layer for accessing cache and other data
 */
public class LocalCacheHandler {
  private static StringCacheService stringCacheService = new StringCacheService();

  public static String getTribelyToken() {
    return stringCacheService.get(CacheKey.TRIBELY_TOKEN);
  }

  public static String getHureyToken() {
    return stringCacheService.get(CacheKey.SHRIS_TOKEN);
  }
}
