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
public class XpmTimesheetAdapter {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private LongProperty id;
  private StringProperty client;
  private StringProperty job;
  private StringProperty task;
  private StringProperty date;
  private StringProperty totalTimeSpent;
  private StringProperty assigneeId;

  public XpmTimesheetAdapter(XpmTimesheet xpmTimesheet) {

    Long id = xpmTimesheet.getId();
    this.id = id != null ? new SimpleLongProperty(id) : null;

    String client = xpmTimesheet.getClient();
    this.client = client != null ? new SimpleStringProperty(client) : null;

    String job = xpmTimesheet.getJob();
    this.job = job != null ? new SimpleStringProperty(job) : null;

    String task = xpmTimesheet.getTask();
    this.task = task != null ? new SimpleStringProperty(task) : null;

    String date = xpmTimesheet.getDate();
    this.date = date != null ? new SimpleStringProperty(date) : null;

    String totalTimeSpent = xpmTimesheet.getTotalTimeSpent();
    this.totalTimeSpent = totalTimeSpent != null ? new SimpleStringProperty(totalTimeSpent) : null;

    String assigneeId = xpmTimesheet.getAssigneeId();
    this.assigneeId = assigneeId != null ? new SimpleStringProperty(assigneeId) : null;

  }
}
