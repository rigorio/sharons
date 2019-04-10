package inc.pabacus.TaskMetrics.api.timesheet.logs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeLog {

  private LogStatus logStatus;
  private String timeStamp;

}
