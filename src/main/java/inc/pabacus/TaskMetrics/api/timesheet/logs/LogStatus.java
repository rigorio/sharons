package inc.pabacus.TaskMetrics.api.timesheet.logs;

public enum LogStatus {
  IN("in"),
  LB("lb"), // lunch break
  BFB("bfb"), // back from break
  OUT("out");

  private String status;

  LogStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
