package inc.pabacus.TaskMetrics.api.tasks;

import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import javafx.beans.property.*;
import lombok.Data;

@Data
public class TaskFXAdapter {

  private LongProperty id;
  private LongProperty projectId;
  private StringProperty projectName;
  private StringProperty title;
  private StringProperty description;
  private StringProperty startTime;
  private StringProperty endTime;
  private StringProperty totalTimeSpent;
  private BooleanProperty billable;
  private DoubleProperty billableHours;
  private DoubleProperty nonBillableHours;

  private StringProperty status;
  private IntegerProperty progress;
  private IntegerProperty priority;
  private StringProperty dateCreated;
  private StringProperty dateCompleted;

  private StringProperty author;

  public TaskFXAdapter(Task task) {
    Long taskId = task.getId();
    id = taskId != null ? new SimpleLongProperty(taskId) : null;

    Long projectId = task.getProjectId();
    this.projectId = projectId != null ? new SimpleLongProperty(projectId) : null;

    String projectName = task.getProjectName();
    this.projectName = projectName != null ? new SimpleStringProperty(projectName) : null;

    String title = task.getTitle();
    this.title = title != null ? new SimpleStringProperty(title) : null;

    String description = task.getDescription();
    this.description = description != null ? new SimpleStringProperty(description) : null;

    String startTime = task.getStartTime();
    this.startTime = status != null ? new SimpleStringProperty(startTime) : null;

    String endTime = task.getEndTime();
    this.endTime = endTime != null ? new SimpleStringProperty(endTime) : null;

    String totalTimeSpent = task.getTotalTimeSpent();
    this.totalTimeSpent = totalTimeSpent != null ? new SimpleStringProperty(totalTimeSpent) : null;

    Boolean billable = task.getBillable();
    this.billable = billable != null ? new SimpleBooleanProperty(billable) : null;

    Double billableHours = task.getBillableHours();
    this.billableHours = billableHours != null ? new SimpleDoubleProperty(billableHours) : null;

    Double nonBillableHours = task.getNonBillableHours();
    this.nonBillableHours = nonBillableHours != null ? new SimpleDoubleProperty(nonBillableHours) : null;

    Status status = task.getStatus();
    this.status = status != null ? new SimpleStringProperty(status.getStatus()) : null;

    Progress progress = task.getProgress();
    this.progress = progress != null ? new SimpleIntegerProperty(progress.getProgress()) : null;

    Priority priority = task.getPriority();
    this.priority = priority != null ? new SimpleIntegerProperty(priority.getPriority()) : null;

    String dateCompleted = task.getDateCompleted();
    this.dateCompleted = dateCompleted != null ? new SimpleStringProperty(dateCompleted) : null;

    String dateCreated = task.getDateCreated();
    this.dateCreated = dateCreated != null ? new SimpleStringProperty(dateCreated) : null;

    String author = task.getAuthor();
    this.author = author != null ? new SimpleStringProperty(author) : null;
  }
}
