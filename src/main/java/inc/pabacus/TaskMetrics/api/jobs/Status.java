package inc.pabacus.TaskMetrics.api.jobs;

public enum Status {

  BACKLOG("Backlog"),
  IN_PROGRESS("In Progress"),
  FOR_REVIEW("For Review"),
  DONE("Closed");

  private String status;

  Status(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
