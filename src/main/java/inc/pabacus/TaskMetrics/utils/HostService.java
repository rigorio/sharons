package inc.pabacus.TaskMetrics.utils;

public class HostService {
  public static String HOST = "http://localhost:8080";

  public static String getHost() {
    return HOST;
  }

  public static void setHOST(String HOST) {
    HostService.HOST = HOST;
  }
}
