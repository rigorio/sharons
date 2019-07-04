package inc.pabacus.TaskMetrics.desktop.newTask;

public class DefaultTaskHolder {
  private static String defaultJob;
  private static String defaultTask;

  public static String getDefaultJob() {
    return defaultJob;
  }

  public static void setDefaultJob(String defaultJob) {
    DefaultTaskHolder.defaultJob = defaultJob;
  }

  public static String getDefaultTask() {
    return defaultTask;
  }

  public static void setDefaultTask(String defaultTask) {
    DefaultTaskHolder.defaultTask = defaultTask;
  }
}
