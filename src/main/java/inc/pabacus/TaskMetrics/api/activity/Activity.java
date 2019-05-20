package inc.pabacus.TaskMetrics.api.activity;

public enum Activity {

  BUSY("Busy"),
  IDLE("Idle"),
  MEETING("Meeting"),
  LUNCH("Lunch"),
  OFFLINE("Offline");

  private String activity;

  Activity(String activity) {
    this.activity = activity;
  }

  public String getActivity() {
    return activity;
  }

  public static Activity convert(String activity) {
    switch (activity) {
      case "Busy":
        return BUSY;
      case "Idle":
        return IDLE;
      case "Meeting":
        return MEETING;
      case "Lunch":
        return LUNCH;
      case "Offline":
        return OFFLINE;
      default:
        return null;
    }
  }
}
