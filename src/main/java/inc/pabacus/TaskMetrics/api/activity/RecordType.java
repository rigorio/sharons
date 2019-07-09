package inc.pabacus.TaskMetrics.api.activity;

public enum RecordType {
  TASK("Task"),
  BREAK("Break"),
  MEETING("Meeting"),
  TRAINING("Training"),
  TECHNICAL_ISSUE("Technical Issue"),
  FIELD_WORK("Field Work");

  private String activity;

  RecordType(String activity) {
    this.activity = activity;
  }

  public String getActivity() {
    return activity;
  }
}
