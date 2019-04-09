package inc.pabacus.TaskMetrics.api.tasks;

import javafx.beans.property.*;
import lombok.Data;

/**
 * TODO add the other fields to the constructor adapter?
 */
@Data
public class TaskFXAdapter {

  private LongProperty id;
  private StringProperty title;
  private StringProperty description;
  private IntegerProperty priority;
  private StringProperty totalTimeSpent;
  private StringProperty status;
  private IntegerProperty progress;
  private StringProperty dateCreated;
  private StringProperty author;

  public TaskFXAdapter(Task task) {
    Long taskId = task.getId();
    id = taskId != null ? new SimpleLongProperty(taskId) : null;
    title = new SimpleStringProperty(task.getTitle());
    description = new SimpleStringProperty(task.getDescription());
    priority = new SimpleIntegerProperty(task.getProgress().getProgress());
    totalTimeSpent = new SimpleStringProperty(task.getTotalTimeSpent());
    status = new SimpleStringProperty(task.getStatus().getStatus());
    progress = new SimpleIntegerProperty(task.getProgress().getProgress());
    dateCreated = new SimpleStringProperty(task.getDateCreated());
    author = new SimpleStringProperty(task.getAuthor());
  }
}
