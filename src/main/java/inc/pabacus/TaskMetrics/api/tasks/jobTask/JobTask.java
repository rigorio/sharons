package inc.pabacus.TaskMetrics.api.tasks.jobTask;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobTask {

  private Long id;
  private String dateCreated;
  private String status;
  private Double timeSpent;
  private String percentage;
  private Long jobsId;
  private Long userId;
  private Job job;

  public JobTask(JobTaskAdapter jobTask) {
    LongProperty id = jobTask.getId();
    this.id = id != null ? id.get() : null;

    StringProperty dateCreated = jobTask.getDateCreated();
    this.dateCreated = dateCreated != null ? dateCreated.get() : null;

    StringProperty status = jobTask.getStatus();
    this.status = status != null ? status.get() : null;

    DoubleProperty timeSpent = jobTask.getTimeSpent();
    this.timeSpent = timeSpent != null ? timeSpent.get() : null;

    StringProperty percentage = jobTask.getPercentage();
    this.percentage = percentage != null ? percentage.get() : null;

    LongProperty jobsId = jobTask.getJobsId();
    this.jobsId = jobsId != null ? jobsId.get() : null;

    LongProperty userId = jobTask.getUserId();
    this.userId = userId != null ? userId.get() : null;
  }

}
