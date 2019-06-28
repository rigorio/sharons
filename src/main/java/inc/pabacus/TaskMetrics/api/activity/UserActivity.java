package inc.pabacus.TaskMetrics.api.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserActivity {

  private Long id;
  private String date;
  private String time;
  private String activity;

  public UserActivity(String dateNow, String timeNow, String activity) {
    this.date = dateNow;
    this.time = timeNow;
    this.activity = activity;
  }

}
