package inc.pabacus.TaskMetrics.api.generateToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqEnt {
  private String tribelyToken;
  private String hureyToken;
  private String employeeId;
  private boolean successful;
  private String error;
}
