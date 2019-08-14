package inc.pabacus.TaskMetrics.api.generateToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HureyResult {
  private String AccessToken;
  private String EncryptedAccessToken;
  private Integer ExpireInSeconds;
  private Integer UserId;
}
