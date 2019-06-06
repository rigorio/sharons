package inc.pabacus.TaskMetrics.desktop.tasks.xpm;

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
public class XpmTaskAdapter {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private LongProperty id;
  private StringProperty title;
  private StringProperty description;
  private StringProperty status;
  private StringProperty time;
  private StringProperty job;
  private StringProperty client;
  private StringProperty assignee;

  public XpmTaskAdapter(XpmTask task) {

    Long id = task.getId();
    this.id = id != null ? new SimpleLongProperty(id) : null;

    String title = task.getTitle();
    this.title = title != null ? new SimpleStringProperty(title) : null;

    String description = task.getDescription();
    this.description = description != null ? new SimpleStringProperty(description) : null;

    String status = task.getStatus();
    this.status = status != null ? new SimpleStringProperty(status) : null;

    String time = task.getTotalTime();
    this.time = time != null ? new SimpleStringProperty(time) : null;

    String job = task.getJob();
    this.job = job != null ? new SimpleStringProperty(job) : null;

    String client = task.getClient();
    this.client = client != null ? new SimpleStringProperty(client) : null;

    String assignee = task.getAssignee();
    this.assignee = assignee != null ? new SimpleStringProperty(assignee) : null;

  }
}
