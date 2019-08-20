package inc.pabacus.TaskMetrics.api.tasks;

import javafx.beans.property.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class XpmTaskAdapter {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private LongProperty id;
  private StringProperty client;
  private StringProperty job;
  private StringProperty description;
  private StringProperty task;
  private StringProperty status;
  private StringProperty dateCreated;
  private StringProperty dateFinished;
  private StringProperty billable;
  private StringProperty startTime;
  private StringProperty endTime;
  private StringProperty estimateTime;
  private StringProperty extendCounter;
  private StringProperty totalTimeSpent;
  private StringProperty percentCompleted;
//  private LongProperty businessValueId;
//  private InvoiceTypeAdapter invoiceTypeAdapter;
//  private AssigneeAdapter assigneeAdapter;
//  private StringProperty assignee;

  public XpmTaskAdapter(XpmTask xpmTask) {

    Long id = xpmTask.getId();
    this.id = id != null ? new SimpleLongProperty(id) : null;

    String client = xpmTask.getClient();
    this.client = client != null ? new SimpleStringProperty(client) : null;

    String job = xpmTask.getJob();
    this.job = job != null ? new SimpleStringProperty(job) : null;

    String description = xpmTask.getDescription();
    this.description = description != null ? new SimpleStringProperty(description) : null;

    String task = xpmTask.getTask();
    this.task = task != null ? new SimpleStringProperty(task) : null;

    String status = xpmTask.getStatus();
    this.status = status != null ? new SimpleStringProperty(status) : null;

    String dateCreated = xpmTask.getDateCreated();
    this.dateCreated = dateCreated != null ? new SimpleStringProperty(dateCreated) : null;

    String dateFinished = xpmTask.getDateFinished();
    this.dateFinished = dateFinished != null ? new SimpleStringProperty(dateFinished) : null;

    String billable = xpmTask.getBillable();
    this.billable = billable != null ? new SimpleStringProperty(billable) : null;

    String startTime = xpmTask.getStartTime();
    this.startTime = startTime != null ? new SimpleStringProperty(startTime) : null;

    String endTime = xpmTask.getEndTime();
    this.endTime = endTime != null ? new SimpleStringProperty(endTime) : null;

    String estimateTime = xpmTask.getEstimateTime();
    this.estimateTime = estimateTime != null ? new SimpleStringProperty(estimateTime) : null;

    String extendCounter = xpmTask.getExtendCounter();
    this.extendCounter = extendCounter != null ? new SimpleStringProperty(extendCounter) : null;

    String totalTimeSpent = xpmTask.getTotalTimeSpent();
    this.totalTimeSpent = totalTimeSpent != null ? new SimpleStringProperty(totalTimeSpent) : null;

    String percentCompleted = xpmTask.getPercentCompleted();
    this.percentCompleted = percentCompleted != null ? new SimpleStringProperty(percentCompleted) : null;

  }
}
