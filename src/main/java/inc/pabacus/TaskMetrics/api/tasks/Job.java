package inc.pabacus.TaskMetrics.api.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {
  private Long id;
  private String job;
}
