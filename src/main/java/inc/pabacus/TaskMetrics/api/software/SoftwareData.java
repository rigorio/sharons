package inc.pabacus.TaskMetrics.api.software;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoftwareData {

  @JsonProperty("DisplayName")
  private String name;
  @JsonProperty("InstallDate")
  private String dateInstalled;
  @JsonProperty("DisplayVersion")
  private String version;

}
