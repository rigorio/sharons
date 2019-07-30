package inc.pabacus.TaskMetrics.api.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignee {
  private Long id;
  private String userName;

  public Assignee(AssigneeAdapter assigneeAdapter) {
    this.id = assigneeAdapter.getId().get();
    this.userName = assigneeAdapter.getAssignee().get();
  }
}
