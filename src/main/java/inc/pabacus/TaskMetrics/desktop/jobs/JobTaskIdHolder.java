package inc.pabacus.TaskMetrics.desktop.jobs;

public class JobTaskIdHolder {
  private static Long id;

  public static Long getId() {
    return id;
  }

  public static void setId(Long id) {
    JobTaskIdHolder.id = id;
  }
}
