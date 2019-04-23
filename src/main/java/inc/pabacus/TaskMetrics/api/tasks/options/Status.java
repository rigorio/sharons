package inc.pabacus.TaskMetrics.api.tasks.options;

public enum Status implements Option {

  BACKLOG("Backlog"),
  IN_PROGRESS("In Progress"),
  FOR_REVIEW("For Review"),
  DONE("Done");

  private String status;

  Status(String status) {
    this.status = status;
  }

  public static Status convert(String s) {
    switch (s) {
      case "Backlog":
        return BACKLOG;
      case "In Progress":
        return IN_PROGRESS;
      case "For review":
        return FOR_REVIEW;
      case "Done":
        return DONE;
      default:
        return null;
    }
  }

  public String getStatus() {
    return status;
  }
}
