package inc.pabacus.TaskMetrics.api.tasks;

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
  private String title;
  private String description;
  private String status;
  private String totalTime;
  private String job;
  private String client;
  private String assignee;
  private String dateCreated;

  public XpmTask(XpmTaskAdapter task) {

    LongProperty id = task.getId();
    this.id = id != null ? id.get() : null;

    StringProperty title = task.getTitle();
    this.title = title != null ? title.get() : null;

    StringProperty description = task.getDescription();
    this.description = description != null ? description.get() : null;

    StringProperty status = task.getStatus();
    this.status = status != null ? status.get() : null;

    StringProperty time = task.getTime();
    this.totalTime = time != null ? time.get() : null;

    StringProperty job = task.getJob();
    this.job = job != null ? job.get() : null;

    StringProperty client = task.getClient();
    this.client = client != null ? client.get() : null;

    StringProperty assignee = task.getAssignee();
    this.assignee = assignee != null ? assignee.get() : null;

    StringProperty dateCreated = task.getDateCreated();
    this.dateCreated = dateCreated != null ? dateCreated.get() : null;


  }
}
