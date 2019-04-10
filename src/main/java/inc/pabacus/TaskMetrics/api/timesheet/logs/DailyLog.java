package inc.pabacus.TaskMetrics.api.timesheet.logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyLog {

  private String date;
  private String in;
  private String otl;
  private String bfl;
  private String out;

}
