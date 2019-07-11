package inc.pabacus.TaskMetrics.api.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityTimestamp {

  private Long id;
  private Long userId;
  private String date;
  private String time;
  private String activity;

  public ActivityTimestamp(String dateNow, String timeNow, String activity) {
    this.date = dateNow;
    this.time = timeNow;
    this.activity = activity;
  }

}