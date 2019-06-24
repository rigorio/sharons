package inc.pabacus.TaskMetrics.api.tasks.jobTask;

import inc.pabacus.TaskMetrics.api.tasks.businessValue.Assignee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobTask {

  private Long id;
  private String client;
  private String job;
  private String description;
  private String task;
  private String status;
  private String date;
  private String totalTime;
  private String businessValue;
  private String invoiceType;
  private Assignee assignee;

}
