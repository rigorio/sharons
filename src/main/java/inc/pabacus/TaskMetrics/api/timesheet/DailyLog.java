package inc.pabacus.TaskMetrics.api.timesheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyLog {

  private String date;
  private TimeLog in;
  private TimeLog otl;
  private TimeLog bfl;
  private TimeLog out;

}
