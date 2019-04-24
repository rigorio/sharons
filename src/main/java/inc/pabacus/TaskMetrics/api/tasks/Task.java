package inc.pabacus.TaskMetrics.api.tasks;

import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {

  private Long id;
  private String title;
  private String description;
  private String totalTimeSpent;
  private Status status;
  private Progress progress;
  private Priority priority;
  private String dateCreated;
  private String dateCompleted;
  private String author;

  public Task(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public Task(String title, String description, String totalTimeSpent) {
    this.title = title;
    this.description = description;
    this.totalTimeSpent = totalTimeSpent;
  }

  public Task(String title, String description, Status status) {
    this.title = title;
    this.description = description;
    this.status = status;
  }

  public Task(TaskFXAdapter task) {
    LongProperty id = task.getId();
    this.id = id != null ? id.get() : null;

    StringProperty title = task.getTitle();
    this.title = title != null ? title.get() : null;

    StringProperty description = task.getDescription();
    this.description = description != null ? description.get() : null;

    StringProperty totalTimeSpent = task.getTotalTimeSpent();
    this.totalTimeSpent = totalTimeSpent != null ? totalTimeSpent.get() : null;

    StringProperty status = task.getStatus();
    this.status = status != null ? Status.convert(status.get()) : null;

    IntegerProperty progress = task.getProgress();
    this.progress = progress != null ? Progress.convert(progress.get()) : null;

    IntegerProperty priority = task.getPriority();
    this.priority = priority != null ? Priority.convert(priority.get()) : null;

    StringProperty dateCreated = task.getDateCreated();
    this.dateCreated = dateCreated != null ? dateCreated.get() : null;

    StringProperty dateCompleted = task.getDateCompleted();
    this.dateCompleted = dateCompleted != null ? dateCompleted.get() : null;

    StringProperty author = task.getAuthor();
    this.author = author != null ? author.get() : null;
  }

}
