package inc.pabacus.TaskMetrics.api.tasks;

import javafx.beans.property.*;
import lombok.Data;

@Data
public class TaskFXAdapter {

  private LongProperty id;
  private StringProperty title;
  private StringProperty description;
  private StringProperty status;
  private IntegerProperty progress;
  private StringProperty dateCreated;
  private StringProperty author;

  public TaskFXAdapter(Task task) {
    Long taskId = task.getId();
    id = taskId != null ? new SimpleLongProperty(taskId) : null;
    title = new SimpleStringProperty(task.getTitle());
    description = new SimpleStringProperty(task.getDescription());
    status = new SimpleStringProperty(task.getStatus().getStatus());
    progress = new SimpleIntegerProperty(task.getProgress().getProgress());
    dateCreated = new SimpleStringProperty(task.getDateCreated());
    author = new SimpleStringProperty(task.getAuthor());
  }
}
