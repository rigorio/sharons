package inc.pabacus.TaskMetrics.api.timesheet;

public enum LogStatus {
  IN("in"),
  OTL("otl"),
  BFL("bfl"),
  OUT("out");

  private String status;

  LogStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
