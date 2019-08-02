package inc.pabacus.TaskMetrics.desktop.newTask;

public class JobHolder {
  private static String job;

  public static String getJob() {
    return job;
  }

  public static void setJob(String job) {
    JobHolder.job = job;
  }
}
