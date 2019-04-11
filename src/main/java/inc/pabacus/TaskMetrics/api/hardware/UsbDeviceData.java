package inc.pabacus.TaskMetrics.api.hardware;

public class UsbDeviceData implements HardwareData {

  private String name;

  private String description;

  public UsbDeviceData() {
  }

  public UsbDeviceData(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }
}
