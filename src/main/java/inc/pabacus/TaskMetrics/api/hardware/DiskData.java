package inc.pabacus.TaskMetrics.api.hardware;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiskData extends HardwareData {
  private String title;
  private String spec;

  public DiskData(String name, String type, String serialNumber, String manufacturer, String title, String spec) {
    super(name, type, serialNumber, manufacturer);
    this.title = title;
    this.spec = spec;
  }
}
