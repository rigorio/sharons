package inc.pabacus.TaskMetrics.api.timesheet.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusUpdate {
  private Long id;
  private Long userId;
  private String date;
  private String time;
  private String status;
}
