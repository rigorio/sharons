package inc.pabacus.TaskMetrics.desktop.jobs;

public class JobTaskIdHolder {
  private static Integer id;

  public static Integer getId() {
    return id;
  }

  public static void setId(Integer id) {
    JobTaskIdHolder.id = id;
  }
}
