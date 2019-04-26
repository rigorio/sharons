package inc.pabacus.TaskMetrics.api.standuply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandupAnswer {
  private Long id;
  private String didYesterday;
  private String doingToday;
  private String obstacles;

  public StandupAnswer(String didYesterday, String doingToday, String obstacles) {
    this.didYesterday = didYesterday;
    this.doingToday = doingToday;
    this.obstacles = obstacles;
  }
}
