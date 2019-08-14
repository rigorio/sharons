package inc.pabacus.TaskMetrics.api.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
  private String billAddress;
  private String client;
  private String id;
  private String billEmail;
  private String billPhoneNumber;
}
