package inc.pabacus.TaskMetrics.api.hardware;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiskData {

  private String name;
  private String type;
  private String serialNumber;
  private String title;
  private String spec;

}
