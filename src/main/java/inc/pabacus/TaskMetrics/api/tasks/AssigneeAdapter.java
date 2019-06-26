package inc.pabacus.TaskMetrics.api.tasks;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssigneeAdapter {
  private LongProperty id;
  private StringProperty assignee;

  public AssigneeAdapter(Assignee assignee) {
    this.id = new SimpleLongProperty(assignee.getAssigneeId());
    this.assignee = new SimpleStringProperty(assignee.getAssignee());
  }
}
