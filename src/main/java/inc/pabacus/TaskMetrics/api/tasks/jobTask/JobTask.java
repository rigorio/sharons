package inc.pabacus.TaskMetrics.api.tasks.jobTask;

import inc.pabacus.TaskMetrics.api.tasks.Client;
import inc.pabacus.TaskMetrics.api.tasks.Job;
import inc.pabacus.TaskMetrics.api.tasks.Task;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.Assignee;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobTask {

  private Long id;
  private Task task;
  private String description;
  private String status;
  private String totalTime;
  private Job job;
  private Client client;
  private BusinessValue businessValue;
  private Assignee assignee;
}
