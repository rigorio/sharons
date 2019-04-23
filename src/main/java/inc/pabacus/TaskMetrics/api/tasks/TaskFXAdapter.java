package inc.pabacus.TaskMetrics.api.tasks;

import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import javafx.beans.property.*;
import lombok.Data;

@Data
public class TaskFXAdapter {

  private LongProperty id;
  private StringProperty title;
  private StringProperty description;
  private StringProperty totalTimeSpent;
  private StringProperty status;
  private IntegerProperty progress;
  private IntegerProperty priority;
  private StringProperty dateCreated;
  private StringProperty author;

  public TaskFXAdapter(Task task) {
    Long taskId = task.getId();
    id = taskId != null ? new SimpleLongProperty(taskId) : null;

    String title = task.getTitle();
    this.title = title != null ? new SimpleStringProperty(title) : null;

    String description = task.getDescription();
    this.description = description != null ? new SimpleStringProperty(description) : null;

    Priority priority = task.getPriority();
    this.priority = progress != null ? new SimpleIntegerProperty(priority.getPriority()) : null;

    String totalTimeSpent = task.getTotalTimeSpent();
    this.totalTimeSpent = totalTimeSpent != null ? new SimpleStringProperty(totalTimeSpent) : null;

    Status status = task.getStatus();
    this.status = status != null ? new SimpleStringProperty(status.getStatus()) : null;

    Progress progress = task.getProgress();
    this.progress = progress != null ? new SimpleIntegerProperty(progress.getProgress()) : null;

    String dateCreated = task.getDateCreated();
    this.dateCreated = dateCreated != null ? new SimpleStringProperty(dateCreated) : null;

    String author = task.getAuthor();
    this.author = author != null ? new SimpleStringProperty(author) : null;
  }
}
