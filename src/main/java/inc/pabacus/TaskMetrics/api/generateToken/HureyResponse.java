package inc.pabacus.TaskMetrics.api.generateToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HureyResponse {
  private Object Result;
  private Object TargetUrl;
  private Boolean Success;
  private Object Error;
  private Boolean UnAuthorizedRequest;
  private Boolean __abp;
}
