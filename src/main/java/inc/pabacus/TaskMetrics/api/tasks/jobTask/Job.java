package inc.pabacus.TaskMetrics.api.tasks.jobTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {
  private long id;
  private String job;
}
