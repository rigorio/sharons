package rigor.io.Sharons.api.gown;

public enum GownStatus {
  RENTED("Rented"),
  BOUGHT("Bought"),
  FOR_PICKUP("For Pickup"),
  DUE_TODAY("Due Today"),
  RETURNED("Returned"),
  OVERDUE("Overdue")
  ;

  private String status;

  GownStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
