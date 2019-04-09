package inc.pabacus.TaskMetrics.api.tasks;

import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * add priority number
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String title;
  private String description;
  private Status status;
  private Progress progress;
  private String dateCreated;
  private String author;

  public Task(String title, String description) {
    this.title = title;
    this.description = description;
  }
}
