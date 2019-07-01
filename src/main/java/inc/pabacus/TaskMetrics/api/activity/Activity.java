package inc.pabacus.TaskMetrics.api.activity;

public enum Activity {

  BUSY("Busy"),
  IDLE("Idle"),
  MEETING("Meeting"),
  LUNCH("Lunch"),
  BREAK("Break"),
  ONLINE("Online"),
  OFFLINE("Offline"),
  IN("Time In"),
  LB("Lunch Break"),
  BFB("Back from Break"),
  OUT("Time Out");

  private String activity;

  Activity(String activity) {
    this.activity = activity;
  }

  public String getActivity() {
    return activity;
  }

  public static Activity convert(String activity) {
    switch (activity) {
      case "Idle":
        return IDLE;
      case "Meeting":
        return MEETING;
      case "Lunch":
        return LUNCH;
      case "Break":
        return BREAK;
      case "Online":
        return ONLINE;
      case "Offline":
        return OFFLINE;
      case "Time In":
        return IN;
      case "Out To Lunch":
        return LB;
      case "Back from Break":
        return BFB;
      case "Time Out":
        return OUT;
      default:
        return BUSY;
    }
  }
}
