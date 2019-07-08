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
  private BooleanProperty billable;
  private StringProperty startTime;
  private StringProperty endTime;
  private StringProperty estimateTime;
  private StringProperty extendCounter;
  private StringProperty totalTimeSpent;
  private StringProperty percentCompleted;
  private LongProperty businessValueId;
  private InvoiceTypeAdapter invoiceTypeAdapter;
  private AssigneeAdapter assigneeAdapter;

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

    Boolean billable = xpmTask.getBillable();
    this.billable = billable != null ? new SimpleBooleanProperty(billable) : null;

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

    Long businessValueId = xpmTask.getBusinessValueId();
    this.businessValueId = businessValueId != null ? new SimpleLongProperty(businessValueId) : null;

    InvoiceType invoiceType = xpmTask.getInvoiceType();
    this.invoiceTypeAdapter = invoiceType != null ? new InvoiceTypeAdapter(invoiceType) : null;

    Assignee assignee = xpmTask.getAssignee();
    this.assigneeAdapter = assignee != null ? new AssigneeAdapter(assignee) : null;
  }
}
