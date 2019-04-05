package inc.pabacus.TaskMetrics.api.tasks.options;

public enum Status implements Option{

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
