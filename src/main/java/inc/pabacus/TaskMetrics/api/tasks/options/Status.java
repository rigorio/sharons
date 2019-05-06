package inc.pabacus.TaskMetrics.api.tasks.options;

public enum Status implements Option {

  BACKLOG("Pending"),
  IN_PROGRESS("In Progress"),
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
