package inc.pabacus.TaskMetrics.api.activity;

public enum Activity {

  BUSY("Busy"),
  IDLE("Idle"),
  MEETING("Meeting"),
  LUNCH("Lunch"),
  BREAK("Break"),
  ONLINE("Logged In"),
  OFFLINE("Logged Out"),
  IN("Time In"),
  OTL("Out To Lunch"),
  BFL("Back From Lunch"),
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
      case "Busy":
        return BUSY;
      case "Idle":
        return IDLE;
      case "Meeting":
        return MEETING;
      case "Lunch":
        return LUNCH;
      case "Break":
        return BREAK;
      case "Logged In":
        return ONLINE;
      case "Logged Out":
        return OFFLINE;
      case "Time In":
        return IN;
      case "Out To Lunch":
        return OTL;
      case "Back From Lunch":
        return BFL;
      case "Time Out":
        return OUT;
      default:
        return null;
    }
  }
}
