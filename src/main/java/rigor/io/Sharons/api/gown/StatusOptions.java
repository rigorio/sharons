package rigor.io.Sharons.api.gown;

public enum StatusOptions {
  OVERDUE("Overdue"),
  DUE_ON("Due On"),
  PICKUP("For Pickup");
  private String status;

  StatusOptions(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
