package inc.pabacus.TaskMetrics.api.timesheet.logs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogItem {
  private Long id;
  private String time;
  private String status;
}
