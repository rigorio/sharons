package inc.pabacus.TaskMetrics.api.tasks;

import javafx.beans.property.BooleanProperty;
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
public class
XpmTask {

  private Long id;
  private String client;
  private String job;
  private String description;
  private String task;
  private String status;
  private String dateCreated;
  private Boolean billable;
  private String startTime;
  private String endTime;
  private String estimateTime;
  private String extendCounter;
  private String totalTimeSpent;
  private Long businessValueId;
  private InvoiceType invoiceType;
  private Assignee assignee;

  public XpmTask(XpmTaskAdapter xpmTask) {

    LongProperty id = xpmTask.getId();
    this.id = id != null ? id.get() : null;

    StringProperty job = xpmTask.getJob();
    this.job = job != null ? job.get() : null;

    StringProperty client = xpmTask.getClient();
    this.client = client != null ? client.get() : null;

    StringProperty description = xpmTask.getDescription();
    this.description = description != null ? description.get() : null;

    StringProperty task = xpmTask.getTask();
    this.task = task != null ? task.get() : null;

    StringProperty status = xpmTask.getStatus();
    this.status = status != null ? status.get() : null;

    StringProperty dateCreated = xpmTask.getDateCreated();
    this.dateCreated = dateCreated != null ? dateCreated.get() : null;

    BooleanProperty billable = xpmTask.getBillable();
    this.billable = billable != null ? billable.get() : null;

    StringProperty startTime = xpmTask.getStartTime();
    this.startTime = startTime != null ? startTime.get() : null;

    StringProperty endTime = xpmTask.getEndTime();
    this.endTime = endTime != null ? endTime.get() : null;

    StringProperty estimateTime = xpmTask.getEstimateTime();
    this.estimateTime = estimateTime != null ? estimateTime.get() : null;

    StringProperty extendCounter = xpmTask.getExtendCounter();
    this.extendCounter = extendCounter != null ? extendCounter.get() : null;

    StringProperty totalTimeSpent = xpmTask.getTotalTimeSpent();
    this.totalTimeSpent = totalTimeSpent != null ? totalTimeSpent.get() : null;

    LongProperty businessValueId = xpmTask.getBusinessValueId();
    this.businessValueId = businessValueId != null ? businessValueId.get() : null;

    InvoiceTypeAdapter invoiceTypeAdapter = xpmTask.getInvoiceTypeAdapter();
    this.invoiceType = invoiceTypeAdapter != null ? new InvoiceType(invoiceTypeAdapter) : null;

    AssigneeAdapter assigneeAdapter = xpmTask.getAssigneeAdapter();
    this.assignee = assigneeAdapter != null ? new Assignee(assigneeAdapter) : null;

  }
}
