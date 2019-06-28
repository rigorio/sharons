package inc.pabacus.TaskMetrics.desktop.tracker;

public class AlwaysOnTopCheckerConfiguration {
  private static boolean alwaysOnTop;

  public static boolean isAlwaysOnTop() {
    return alwaysOnTop;
  }

  public static void setAlwaysOnTop(boolean alwaysOnTop) {
    AlwaysOnTopCheckerConfiguration.alwaysOnTop = alwaysOnTop;
  }
}
