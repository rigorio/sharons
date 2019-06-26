package inc.pabacus.TaskMetrics.api.tasks.jobTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
  private Long id;
  private String task;
  private Long jobId;
}
