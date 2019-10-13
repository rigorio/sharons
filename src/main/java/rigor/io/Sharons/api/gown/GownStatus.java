package rigor.io.Sharons.api.gown;

public enum GownStatus {
  RENTED("Rented"),
  AVAILABLE("Available"),
  BOUGHT("Bought"),
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
