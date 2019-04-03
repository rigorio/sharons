package inc.pabacus.TaskMetrics.api.hardware;

public enum Device {
  KEYBOARD("keyboard"),
  MOUSE("mouse"),
  AUDIO("audio");


  private String device;

  Device(String device) {
    this.device = device;
  }

  public String getDevice() {
    return device;
  }
}
