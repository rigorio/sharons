package inc.pabacus.TaskMetrics.api.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignee {
  private Long assigneeId;
  private String assignee;

  public Assignee(AssigneeAdapter assigneeAdapter) {
    this.assigneeId = assigneeAdapter.getId().get();
    this.assignee = assigneeAdapter.getAssignee().get();
  }
}
