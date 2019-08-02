package inc.pabacus.TaskMetrics.api.generateToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqEnt {
  private String tribelyToken;
  private String hureyToken;
  private String employeeId;
}
