package inc.pabacus.TaskMetrics.api.kicker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KickStatus {
  private String status;
  private String oldToken;
  private String newToken;
}
