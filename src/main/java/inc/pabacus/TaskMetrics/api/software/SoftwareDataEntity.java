package inc.pabacus.TaskMetrics.api.software;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareDataEntity {
  private String timeStamp;
  private List<SoftwareData> runningSoftwares;
}
