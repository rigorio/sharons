package inc.pabacus.TaskMetrics.desktop.taskTimesheet.xpmTimesheet;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
public class XpmTimesheet {

  private Long id;
  private String client;
  private String job;
  private String task;
  private String date;
  private String totalTimeSpent;
  private String assigneeId;

  public XpmTimesheet(XpmTimesheetAdapter xpmTimesheetAdapter) {

    LongProperty id = xpmTimesheetAdapter.getId();
    this.id = id != null ? id.get() : null;

    StringProperty client = xpmTimesheetAdapter.getClient();
    this.client = client != null ? client.get() : null;

    StringProperty job = xpmTimesheetAdapter.getJob();
    this.job = job != null ? job.get() : null;

    StringProperty task = xpmTimesheetAdapter.getTask();
    this.task = task != null ? task.get() : null;

    StringProperty date = xpmTimesheetAdapter.getDate();
    this.date = date != null ? date.get() : null;

    StringProperty totalTimeSpent = xpmTimesheetAdapter.getTotalTimeSpent();
    this.totalTimeSpent = totalTimeSpent != null ? totalTimeSpent.get() : null;

    StringProperty assigneeId = xpmTimesheetAdapter.getAssigneeId();
    this.assigneeId = assigneeId != null ? assigneeId.get() : null;


  }
}
