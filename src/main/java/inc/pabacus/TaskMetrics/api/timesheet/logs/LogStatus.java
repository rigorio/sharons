package inc.pabacus.TaskMetrics.api.timesheet.logs;

public enum LogStatus {
  IN("Time In"),
  LB("Lunch Break"), // lunch break
  BFB("Back From Break"), // back from break
  OUT("Time Out");

  private String status;

  LogStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
