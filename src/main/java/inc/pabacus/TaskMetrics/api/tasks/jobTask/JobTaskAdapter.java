package inc.pabacus.TaskMetrics.api.tasks.jobTask;

import javafx.beans.property.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.net.ssl.SSLParameters;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobTaskAdapter {

  private LongProperty id;
  private StringProperty jobNumber;
  private StringProperty description;
  private StringProperty dateCreated;
  private StringProperty status;
  private DoubleProperty timeSpent;
  private StringProperty percentage;
  private LongProperty jobsId;
  private LongProperty userId;
  private StringProperty jobName;

  public JobTaskAdapter(JobTask jobTask) {
    Long id = jobTask.getId();
    this.id = id != null ? new SimpleLongProperty(id) : null;

    String jobNumber = jobTask.getJobNumber();
    this.jobNumber = jobNumber != null ? new SimpleStringProperty(jobNumber) : null;

    String description = jobTask.getDescription();
    this.description = description != null ? new SimpleStringProperty(description) : null;

    String dateCreated = jobTask.getDateCreated();
    this.dateCreated = dateCreated != null ? new SimpleStringProperty(dateCreated) : null;

    String status = jobTask.getStatus();
    this.status = status != null ? new SimpleStringProperty(status) : null;

    Double timeSpent = jobTask.getTimeSpent();
    this.timeSpent = timeSpent != null ? new SimpleDoubleProperty(timeSpent) : null;

    String percentage = jobTask.getPercentage();
    this.percentage = percentage != null ? new SimpleStringProperty(percentage) : null;

    Long jobsId = jobTask.getJobsId();
    this.jobsId = jobsId != null ? new SimpleLongProperty(jobsId) : null;

    Long userId = jobTask.getUserId();
    this.userId = userId != null ? new SimpleLongProperty(userId) : null;

    jobName = new SimpleStringProperty(jobTask.getJob().getJob());
  }

}
