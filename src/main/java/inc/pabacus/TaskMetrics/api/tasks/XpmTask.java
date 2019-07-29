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
                                                    /**/
  private Long id;                                  /*       "id": 35,*/
  private String client;                            /*        "client": "DBA",*/
  private String job;                               /*        "job": "Tribely",*/
  private String description;                       /*        "description": "",*/
  private String task;                              /*        "task": "System Development",*/
  private String status;                            /*        "status": "Pending",*/
  private String dateCreated;                       /*        "dateCreated": "2019-07-29",*/
  private String dateFinished;                      /*        "dateFinished": null,*/
  private Boolean billable;                         /*        "billable": "true",*/
  private String startTime;                         /*        "startTime": null,*/
  private String endTime;                           /*        "endTime": null,*/
  private String estimateTime;                      /*        "estimateTime": 0,*/
  private String extendCounter;                     /*        "extendCounter": null,*/
  private String totalTimeSpent;                    /*        "totalTimeSpent": 0,*/
  private String percentCompleted;                  /*        "percentCompleted": "0%"*/
//  private Long businessValueId;
//  private InvoiceType invoiceTypeId;
//  private Assignee assigneeId;
//  private String assignee;

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

    StringProperty dateFinished = xpmTask.getDateFinished();
    this.dateFinished = dateFinished != null ? dateFinished.get() : null;

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

    StringProperty percentCompleted = xpmTask.getPercentCompleted();
    this.percentCompleted = percentCompleted != null ? percentCompleted.get() : null;

  }
}
