package inc.pabacus.TaskMetrics.api.jobs;

import javafx.beans.property.*;
import lombok.Data;

@Data
public class JobFXAdapter {

  private LongProperty id;
  private StringProperty title;
  private StringProperty description;
  private StringProperty status;
  private IntegerProperty progress;
  private StringProperty dateCreated;
  private StringProperty author;

  public JobFXAdapter(Job job) {
    id = new SimpleLongProperty(job.getId());
    title = new SimpleStringProperty(job.getTitle());
    description = new SimpleStringProperty(job.getDescription());
    status = new SimpleStringProperty(job.getStatus().getStatus());
    progress = new SimpleIntegerProperty(job.getProgress().getProgress());
    dateCreated = new SimpleStringProperty(job.getDateCreated());
    author = new SimpleStringProperty(job.getAuthor());
  }
}
